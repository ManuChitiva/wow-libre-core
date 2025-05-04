package com.register.wowlibre.application.services.characters;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.characters.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.server_services.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CharactersService implements CharactersPort {
    private final IntegratorPort integratorService;
    private final AccountGamePort accountGamePort;
    private final PasswordEncoder passwordEncoder;
    private final ServerServicesPort serverServicesPort;

    public CharactersService(IntegratorPort integratorService, AccountGamePort accountGamePort,
                             PasswordEncoder passwordEncoder, ServerServicesPort serverServicesPort) {
        this.integratorService = integratorService;
        this.accountGamePort = accountGamePort;
        this.passwordEncoder = passwordEncoder;
        this.serverServicesPort = serverServicesPort;
    }


    @Override
    public CharactersDto characters(Long userId, Long accountId, Long serverId, String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.characters(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(),
                accountId, userId, transactionId);
    }

    @Override
    public CharactersDto loanApplicationCharacters(Long userId, Long accountId, Long serverId, String transactionId) {
        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.loanApplicationCharacters(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(),
                accountId, userId, transactionId);
    }

    @Override
    public void deleteFriend(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                             String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);


        integratorService.deleteFriend(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(),
                characterId, friendId, accountId, userId, transactionId);
    }


    @Override
    public MailsDto mails(Long userId, Long accountId, Long serverId, Long characterId, String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.mails(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(), characterId, transactionId);
    }

    @Override
    public CharacterSocialDto friends(Long userId, Long accountId, Long serverId, Long characterId,
                                      String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.friends(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(), characterId, transactionId);
    }


    @Override
    public void changePassword(Long userId, Long accountId, Long serverId, String password, String newPassword,
                               String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        AccountGameEntity accountGameModel = accountVerificationDto.accountGame();
        Long userIdAccount = accountGameModel.getUserId().getId();
        RealmEntity server = accountVerificationDto.server();

        if (!passwordEncoder.matches(password, accountVerificationDto.accountGame().getUserId().getPassword())) {
            throw new InternalException("The password is invalid", transactionId);
        }
        Integer expansionId = Integer.valueOf(server.getExpansion());
        integratorService.changePassword(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getApiSecret(),
                accountVerificationDto.server().getJwt(),
                accountId, userIdAccount, newPassword, expansionId, transactionId);
    }

    @Override
    public List<CharacterProfessionsDto> professions(Long userId, Long accountId, Long serverId, Long characterId,
                                                     String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.professions(accountVerificationDto.server().getIp(),
                accountVerificationDto.server().getJwt(), accountId, characterId, transactionId);
    }

    @Override
    public void sendLevel(Long userId, Long accountId, Long serverId, Long characterId, Long friendId, Integer level,
                          String transactionId) {

        AccountVerificationDto verifyAccount = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyAccount.server();
        final AccountGameEntity accountGame = verifyAccount.accountGame();

        if (!serverModel.isStatus()) {
            throw new InternalException("The server is currently not verified", transactionId);
        }

        ServerServicesModel serverServicesModel =
                serverServicesPort.findByNameAndServerId(RealmServices.SEND_LEVEL.getName(), serverId, transactionId);

        double cost = 0.0;
        if (serverServicesModel != null && serverServicesModel.amount() > 0) {
            cost = serverServicesModel.amount();
        }

        integratorService.sendLevel(serverModel.getIp(), serverModel.getJwt(), accountGame.getAccountId(),
                accountGame.getUserId().getId(), characterId, friendId, level, cost, transactionId);
    }

    @Override
    public void sendMoney(Long userId, Long accountId, Long serverId, Long characterId, Long friendId, Long money,
                          String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.server();
        final AccountGameEntity accountGame = verifyData.accountGame();

        if (!serverModel.isStatus()) {
            throw new InternalException("The server is currently not verified", transactionId);
        }


        integratorService.sendMoney(serverModel.getIp(), serverModel.getJwt(), accountGame.getAccountId(),
                accountGame.getUserId().getId(), characterId, friendId, money, 0.0, transactionId);
    }

    @Override
    public void sendAnnouncement(Long userId, Long accountId, Long serverId, Long characterId, Long skillId,
                                 String message,
                                 String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.server();

        if (!serverModel.isStatus()) {
            throw new InternalException("The server is currently not verified", transactionId);
        }

        integratorService.sendAnnouncement(serverModel.getIp(), serverModel.getJwt(), userId, accountId, characterId,
                skillId, message, transactionId);
    }

    @Override
    public List<CharacterInventoryResponse> getCharacterInventory(Long userId, Long accountId, Long serverId,
                                                                  Long characterId, String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.server();

        return integratorService.getCharacterInventory(serverModel.getIp(), serverModel.getJwt(), characterId,
                accountId,
                transactionId);
    }

    @Override
    public void transferInventoryItem(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                                      Integer count, Long itemId, String transactionId) {

        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.server();

        integratorService.transferInventoryItem(serverModel.getIp(), serverModel.getJwt(), accountId, characterId,
                friendId, count, itemId, transactionId);
    }


}
