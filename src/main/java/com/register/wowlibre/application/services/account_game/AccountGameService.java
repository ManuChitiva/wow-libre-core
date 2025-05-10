package com.register.wowlibre.application.services.account_game;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.account_game.AccountsGameDto;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.account_game.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class AccountGameService implements AccountGamePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountGameService.class);

    /**
     * Account Game PORT
     **/
    private final SaveAccountGamePort saveAccountGamePort;
    private final ObtainAccountGamePort obtainAccountGamePort;
    /**
     * USERS PORT
     **/
    private final UserPort userPort;
    /**
     * Realm PORT
     **/
    private final RealmPort realmPort;
    /**
     * EXTERNAL CLIENT PORT
     **/
    private final IntegratorPort integratorPort;

    public AccountGameService(SaveAccountGamePort saveAccountGamePort, ObtainAccountGamePort obtainAccountGamePort,
                              RealmPort realmPort, UserPort userPort,
                              IntegratorPort integratorPort) {
        this.saveAccountGamePort = saveAccountGamePort;
        this.obtainAccountGamePort = obtainAccountGamePort;
        this.realmPort = realmPort;
        this.userPort = userPort;
        this.integratorPort = integratorPort;
    }

    @Override
    public void create(Long userId, String realmName, Integer expansionId, String username, String password,
                       String transactionId) {

        Optional<UserEntity> userModel = userPort.findByUserId(userId, transactionId);

        if (userModel.isEmpty()) {
            throw new UnauthorizedException("The client is not available or does not exist", transactionId);
        }

        UserEntity user = userModel.get();

        RealmModel realmModel = realmPort.findByNameAndVersionAndStatusIsTrue(realmName, expansionId,
                transactionId);

        if (realmModel == null) {
            LOGGER.error("[AccountGameService] [create] Server {} with expansion {} not found", realmName,
                    expansionId);
            throw new InternalException("It is not possible to register on any realm, they are currently not " +
                    "available", transactionId);
        }

        if (obtainAccountGamePort.findByUserIdAndRealmId(userId, realmModel.id, transactionId).size() >= 20) {
            LOGGER.error("[AccountGameService] [create] User {} has reached the " +
                    "maximum number of accounts on realm {}", userId, realmModel.id);
            throw new InternalException("You cannot create more than 20 accounts per realm", transactionId);
        }

        Long accountId = integratorPort.createAccount(realmModel.ip, realmModel.apiSecret, realmModel.expansion,
                username, password, user.getEmail(), user.getId(), transactionId);

        AccountGameEntity accountGameEntity = new AccountGameEntity();
        accountGameEntity.setAccountId(accountId);
        accountGameEntity.setRealmId(RealmMapper.toEntity(realmModel));
        accountGameEntity.setUserId(user);
        accountGameEntity.setUsername(username);
        accountGameEntity.setStatus(true);
        saveAccountGamePort.save(accountGameEntity, transactionId);
    }


    @Override
    public AccountsGameDto accounts(Long userId, int page, int size, String searchUsername, String realmName,
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
            if (searchUsername != null || realmName != null) {
                accountsGame = obtainAccountGamePort.findByUserIdAndRealmNameAndUsernameStatusIsTrue(userId, page,
                        size, realmName, searchUsername, transactionId).stream().map(this::mapToModel).toList();
            } else {
                accountsGame = obtainAccountGamePort.findByUserIdAndStatusIsTrue(userId,
                        page, size, transactionId).stream().map(this::mapToModel).toList();
            }
        }


        return new AccountsGameDto(accountsGame, sizeAccounts);
    }


    @Override
    public AccountsGameDto accounts(Long userId, Long realmId, String transactionId) {

        if (userPort.findByUserId(userId, transactionId).isEmpty()) {
            throw new InternalException("The client is not available or does not exist", transactionId);
        }

        List<AccountGameModel> accountsGame = obtainAccountGamePort.findByUserIdAndRealmId(userId, realmId,
                transactionId).stream().map(this::mapToModel).toList();

        return new AccountsGameDto(accountsGame, (long) accountsGame.size());
    }

    @Override
    public AccountVerificationDto verifyAccount(Long userId, Long accountId, Long realmId, String transactionId) {

        Optional<RealmEntity> realm = realmPort.findById(realmId, transactionId);

        if (realm.isEmpty()) {
            throw new InternalException("The realm where your character is currently located is not available",
                    transactionId);
        }

        Optional<AccountGameEntity> accountGame =
                obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId,
                        realm.get().getId(), transactionId);

        if (accountGame.isEmpty()) {
            throw new InternalException("Currently your account is not found or is not available, please contact " +
                    "support", transactionId);
        }
        return new AccountVerificationDto(realm.get(), accountGame.get());
    }

    @Override
    public AccountGameDetailDto account(Long userId, Long accountId, Long realmId, String transactionId) {

        Optional<RealmEntity> realm = realmPort.findById(realmId, transactionId);

        if (realm.isEmpty() || !realm.get().isStatus()) {
            throw new InternalException("The realm where your character is currently located is not available",
                    transactionId);
        }

        final RealmEntity realmDetail = realm.get();

        Optional<AccountGameEntity> accountGame =
                obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId, realmId,
                        transactionId);

        if (accountGame.isEmpty()) {
            throw new InternalException("Currently your account is not found or is not available, please contact " +
                    "support", transactionId);
        }

        AccountDetailResponse account = integratorPort.account(realmDetail.getHost(), realmDetail.getJwt(),
                accountId, transactionId);


        return AccountGameDetailDto.builder()
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
                .realm(realmDetail.getName())
                .failedLogins(account.failedLogins())
                .accountBanned(account.accountBanned()).build();
    }


    private AccountGameModel mapToModel(AccountGameEntity accountGameEntity) {
        boolean status = accountGameEntity.isStatus() && accountGameEntity.getRealmId().isStatus();
        Expansion expansion = Expansion.getById(accountGameEntity.getRealmId().getExpansionId());
        return new AccountGameModel(accountGameEntity.getId(), accountGameEntity.getUsername(),
                accountGameEntity.getAccountId(), accountGameEntity.getUserId().getEmail(),
                accountGameEntity.getRealmId().getName(), accountGameEntity.getRealmId().getId(),
                expansion.getName(), accountGameEntity.getRealmId().getAvatarUrl(),
                accountGameEntity.getRealmId().getWeb(), status, accountGameEntity.getRealmId().getRealmlist());
    }
}
