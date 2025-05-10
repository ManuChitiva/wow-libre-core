package com.register.wowlibre.application.services.characters;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.account_game.*;
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

        return integratorService.characters(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(),
                accountId, userId, transactionId);
    }

    @Override
    public CharactersDto loanApplicationCharacters(Long userId, Long accountId, Long serverId, String transactionId) {
        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.loanApplicationCharacters(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(),
                accountId, userId, transactionId);
    }

    @Override
    public void deleteFriend(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                             String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);


        integratorService.deleteFriend(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(),
                characterId, friendId, accountId, userId, transactionId);
    }


    @Override
    public MailsDto mails(Long userId, Long accountId, Long serverId, Long characterId, String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.mails(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(), characterId, transactionId);
    }

    @Override
    public CharacterSocialDto friends(Long userId, Long accountId, Long serverId, Long characterId,
                                      String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.friends(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(), characterId, transactionId);
    }


    @Override
    public void changePassword(Long userId, Long accountId, Long serverId, String password, String newPassword,
                               String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        AccountGameEntity accountGameModel = accountVerificationDto.accountGame();
        Long userIdAccount = accountGameModel.getUserId().getId();
        RealmEntity server = accountVerificationDto.realm();

        if (!passwordEncoder.matches(password, accountVerificationDto.accountGame().getUserId().getPassword())) {
            throw new InternalException("The password is invalid", transactionId);
        }

        Integer expansionId = server.getExpansionId();
        integratorService.changePassword(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getApiSecret(),
                accountVerificationDto.realm().getJwt(),
                accountId, userIdAccount, newPassword, expansionId, transactionId);
    }

    @Override
    public List<CharacterProfessionsDto> professions(Long userId, Long accountId, Long serverId, Long characterId,
                                                     String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        return integratorService.professions(accountVerificationDto.realm().getHost(),
                accountVerificationDto.realm().getJwt(), accountId, characterId, transactionId);
    }

    @Override
    public void sendLevel(Long userId, Long accountId, Long realmId, Long characterId, Long friendId, Integer level,
                          String transactionId) {

        AccountVerificationDto verifyAccount = accountGamePort.verifyAccount(userId, accountId, realmId,
                transactionId);

        final RealmEntity realm = verifyAccount.realm();
        final AccountGameEntity accountGame = verifyAccount.accountGame();

        if (!realm.isStatus()) {
            throw new InternalException("The realm is currently not verified", transactionId);
        }

        ServerServicesModel serverServicesModel =
                serverServicesPort.findByNameAndServerId(RealmServices.SEND_LEVEL, realmId, transactionId);

        double cost = 0.0;
        if (serverServicesModel != null && serverServicesModel.amount() > 0) {
            cost = serverServicesModel.amount();
        }

        integratorService.sendLevel(realm.getHost(), realm.getJwt(), accountGame.getAccountId(),
                accountGame.getUserId().getId(), characterId, friendId, level, cost, transactionId);
    }

    @Override
    public void sendMoney(Long userId, Long accountId, Long serverId, Long characterId, Long friendId, Long money,
                          String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.realm();
        final AccountGameEntity accountGame = verifyData.accountGame();

        if (!serverModel.isStatus()) {
            throw new InternalException("The realm is currently not verified", transactionId);
        }

        integratorService.sendMoney(serverModel.getHost(), serverModel.getJwt(), accountGame.getAccountId(),
                accountGame.getUserId().getId(), characterId, friendId, money, 0.0, transactionId);
    }

    @Override
    public void sendAnnouncement(Long userId, Long accountId, Long serverId, Long characterId, Long skillId,
                                 String message,
                                 String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.realm();

        if (!serverModel.isStatus()) {
            throw new InternalException("The realm is currently not verified", transactionId);
        }

        integratorService.sendAnnouncement(serverModel.getHost(), serverModel.getJwt(), userId, accountId, characterId,
                skillId, message, transactionId);
    }

    @Override
    public List<CharacterInventoryResponse> getCharacterInventory(Long userId, Long accountId, Long serverId,
                                                                  Long characterId, String transactionId) {
        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.realm();

        return integratorService.getCharacterInventory(serverModel.getHost(), serverModel.getJwt(), characterId,
                accountId,
                transactionId);
    }

    @Override
    public void transferInventoryItem(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                                      Integer count, Long itemId, String transactionId) {

        AccountVerificationDto verifyData = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        final RealmEntity serverModel = verifyData.realm();

        integratorService.transferInventoryItem(serverModel.getHost(), serverModel.getJwt(), accountId, characterId,
                friendId, count, itemId, transactionId);
    }


}
