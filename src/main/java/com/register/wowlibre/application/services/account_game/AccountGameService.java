package com.register.wowlibre.application.services.account_game;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.server.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.account_game.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class AccountGameService implements AccountGamePort {
    private final SaveAccountGamePort saveAccountGamePort;
    private final ObtainAccountGamePort obtainAccountGamePort;
    private final ServerPort serverPort;
    private final UserPort userPort;
    private final IntegratorPort integratorPort;

    public AccountGameService(SaveAccountGamePort saveAccountGamePort, ObtainAccountGamePort obtainAccountGamePort,
                              ServerPort serverPort, UserPort userPort,
                              IntegratorPort integratorPort) {
        this.saveAccountGamePort = saveAccountGamePort;
        this.obtainAccountGamePort = obtainAccountGamePort;
        this.serverPort = serverPort;
        this.userPort = userPort;
        this.integratorPort = integratorPort;
    }

    @Override
    public AccountsDto accounts(Long userId, int page, int size, String searchUsername, String serverName,
                                String transactionId) {
        if (size > 30) {
            size = 30;
        }

        if (userPort.findByUserId(userId, transactionId).isEmpty()) {
            throw new UnauthorizedException("The client is not available or does not exist", transactionId);
        }

        Long sizeAccounts = obtainAccountGamePort.accounts(userId);
        List<AccountGameModel> accountsGame = new ArrayList<>();

        if (sizeAccounts > 0) {
            if (searchUsername != null || serverName != null) {
                accountsGame = obtainAccountGamePort.findByUserIdAndServerNameAndUsernameStatusIsTrue(userId, page,
                        size, serverName, searchUsername, transactionId).stream().map(this::mapToModel).toList();
            } else {
                accountsGame = obtainAccountGamePort.findByUserIdAndStatusIsTrue(userId,
                        page, size, transactionId).stream().map(this::mapToModel).toList();
            }
        }


        return new AccountsDto(accountsGame, sizeAccounts);
    }

    @Override
    public void create(Long userId, String serverName, String expansion, String username, String password,
                       String transactionId) {

        Optional<UserEntity> userModel = userPort.findByUserId(userId, transactionId);

        if (userModel.isEmpty()) {
            throw new UnauthorizedException("The client is not available or does not exist", transactionId);
        }

        UserEntity user = userModel.get();

        if (!user.getVerified()) {
            throw new InternalException("Currently your account is not validated, please validate", transactionId);
        }

        ServerModel serverAvailable = serverPort.findByNameAndVersionAndStatusIsTrue(serverName, expansion,
                transactionId);

        if (serverAvailable == null) {
            throw new InternalException("It is not possible to register on any server, they are currently not " +
                    "available", transactionId);
        }

        Long accountId = integratorPort.create(serverAvailable.ip, serverAvailable.apiSecret, serverAvailable.expansion,
                username, password, user.getEmail(), user.getId(), transactionId);

        AccountGameEntity accountGameEntity = new AccountGameEntity();
        accountGameEntity.setAccountId(accountId);
        accountGameEntity.setServerId(ServerMapper.toEntity(serverAvailable));
        accountGameEntity.setUserId(user);
        accountGameEntity.setUsername(username);
        accountGameEntity.setStatus(true);
        saveAccountGamePort.save(accountGameEntity, transactionId);
    }


    @Override
    public AccountsDto accounts(Long userId, Long serverId, String transactionId) {

        if (userPort.findByUserId(userId, transactionId).isEmpty()) {
            throw new InternalException("The client is not available or does not exist", transactionId);
        }

        Long sizeAccounts = obtainAccountGamePort.accounts(userId);
        List<AccountGameModel> accountsGame = obtainAccountGamePort.findByUserIdAndServerId(userId, serverId,
                transactionId).stream().map(this::mapToModel).toList();

        return new AccountsDto(accountsGame, sizeAccounts);
    }

    @Override
    public AccountVerificationDto verifyAccount(Long userId, Long accountId, Long serverId, String transactionId) {

        Optional<ServerEntity> server = serverPort.findById(serverId, transactionId);

        if (server.isEmpty()) {
            throw new InternalException("The server where your character is currently located is not available",
                    transactionId);
        }

        Optional<AccountGameEntity> accountGame =
                obtainAccountGamePort.findByUserIdAndAccountIdAndStatusIsTrue(userId, accountId, transactionId);

        if (accountGame.isEmpty()) {
            throw new InternalException("Currently your account is not found or is not available, please contact " +
                    "support",
                    transactionId);
        }

        return new AccountVerificationDto(server.get(), accountGame.get());
    }

    @Override
    public AccountDetailDto account(Long userId, Long accountId, Long serverId, String transactionId) {
        final ServerEntity serverRequest = getServer(serverId, transactionId);

        Optional<AccountGameEntity> accountGame =
                obtainAccountGamePort.findByUserIdAndAccountIdAndStatusIsTrue(userId, accountId, transactionId);

        if (accountGame.isEmpty()) {
            throw new InternalException("Currently your account is not found or is not available, please contact " +
                    "support",
                    transactionId);
        }

        AccountDetailResponse account = integratorPort.account(serverRequest.getIp(),
                serverRequest.getJwt(), accountId, transactionId);


        return AccountDetailDto.builder()
                .username(account.username())
                .expansion(account.expansion())
                .id(account.id())
                .email(account.email())
                .os(account.os())
                .lastIp(account.lastIp())
                .lastLogin(account.lastLogin())
                .joinDate(account.joinDate())
                .muteBy(account.muteBy())
                .muteReason(account.muteReason())
                .mute(account.mute())
                .online(account.online())
                .server(serverRequest.getName())
                .failedLogins(account.failedLogins())
                .accountBanned(account.accountBanned()).build();
    }



    private ServerEntity getServer(Long serverId, String transactionId) {
        Optional<ServerEntity> server = serverPort.findById(serverId, transactionId);

        if (server.isEmpty() || !server.get().isStatus()) {
            throw new InternalException("The server where your character is currently located is not available",
                    transactionId);
        }

        return server.get();
    }

    private AccountGameModel mapToModel(AccountGameEntity accountGameEntity) {
        boolean status = accountGameEntity.isStatus() && accountGameEntity.getServerId().isStatus();
        Expansion expansion = Expansion.getById(Integer.parseInt(accountGameEntity.getServerId().getExpansion()));
        return new AccountGameModel(accountGameEntity.getId(), accountGameEntity.getUsername(),
                accountGameEntity.getAccountId(), accountGameEntity.getUserId().getEmail(),
                accountGameEntity.getServerId().getName(), accountGameEntity.getServerId().getId(),
                expansion.getDisplayName()
                , accountGameEntity.getServerId().getAvatar(),
                accountGameEntity.getServerId().getWebSite(), status);
    }
}
