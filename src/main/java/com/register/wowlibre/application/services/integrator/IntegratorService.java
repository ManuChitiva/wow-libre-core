package com.register.wowlibre.application.services.integrator;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.dto.guilds.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.client.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.util.*;

@Service
public class IntegratorService implements IntegratorPort {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(IntegratorService.class);

    private final IntegratorClient integratorClient;

    public IntegratorService(IntegratorClient integratorClient) {
        this.integratorClient = integratorClient;
    }

    @Override
    public Long createAccount(String host, String apiSecret, Integer expansion, String username, String password,
                              String email, Long userId, String transactionId) {
        byte[] salt = KeyDerivationUtil.generateSalt();
        String encryptedMessage;

        try {
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
            encryptedMessage = EncryptionUtil.encrypt(password, derivedKey);
        } catch (Exception e) {
            LOGGER.error("It was not possible to create the account on the realm. userId: {} transactionId {}",
                    userId, transactionId);
            throw new InternalException("Could not create account for realm", transactionId);
        }

        AccountGameCreateRequest accountGameCreateRequest = AccountGameCreateRequest.builder()
                .username(username).password(encryptedMessage)
                .email(email).userId(userId)
                .expansionId(expansion).salt(salt).build();

        return integratorClient.createAccountGame(host, accountGameCreateRequest, transactionId);
    }


    @Override
    public CharactersDto characters(String host, String jwt, Long accountId, Long userId, String transactionId) {
        CharactersResponse response = integratorClient.characters(host, jwt, accountId, transactionId);

        if (response == null) {
            LOGGER.error("It was not possible to get the characters from the realm.  Host: {} userId: {}",
                    host, userId);
            throw new InternalException("It was not possible to get the characters from the realm.", transactionId);
        }

        CharactersDto characters = new CharactersDto();
        characters.setCharacters(response.getCharacters());
        characters.setTotalQuantity(response.getTotalQuantity());

        return characters;
    }

    @Override
    public AccountDetailResponse account(String host, String jwt, Long accountId, String transactionId) {
        AccountDetailResponse accountDetailResponse = integratorClient.account(host, jwt, accountId, transactionId);

        if (accountDetailResponse == null) {
            LOGGER.error("Could not get account details for this realm.  Host: {} accountId: {} " +
                    "transactionId {}", host, accountId, transactionId);
            throw new InternalException("It was not possible to obtain the account details for this realm",
                    transactionId);
        }

        return accountDetailResponse;
    }

    //@Cacheable(value = "mails", key = "#characterId")
    @Override
    public MailsDto mails(String host, String jwt, Long characterId, String transactionId) {
        MailsResponse mailsResponse = integratorClient.mails(host, jwt, characterId, transactionId);

        if (mailsResponse == null) {
            LOGGER.error("It was not possible to obtain the emails for this realm.  Host: {} characterId: {} " +
                    "transactionId {}", host, characterId, transactionId);
            throw new InternalException("It was not possible to obtain the emails for this realm",
                    transactionId);
        }

        return new MailsDto(mailsResponse.getMails(), mailsResponse.getSize());
    }

    @Override
    public void deleteFriend(String host, String jwt, Long characterId, Long friendId, Long accountId, Long userId,
                             String transactionId) {
        integratorClient.deleteFriend(host, jwt, characterId, friendId, accountId, userId, transactionId);
    }

    @Override
    public CharacterSocialDto friends(String host, String jwt, Long characterId, String transactionId) {
        CharacterSocialResponse characterSocialResponse = integratorClient.friends(host, jwt, characterId,
                transactionId);

        if (characterSocialResponse == null) {
            LOGGER.error("It was not possible to obtain the list of friends on this realm.  Host: {} characterId: {}" +
                    " " +
                    "transactionId {}", host, characterId, transactionId);
            throw new InternalException("It was not possible to obtain the list of friends on this realm",
                    transactionId);
        }

        return new CharacterSocialDto(characterSocialResponse.getFriends(), characterSocialResponse.getTotalQuantity());
    }

    @Override
    public void changePassword(String host, String apiSecret, String jwt, Long accountId, Long userId,
                               String password, Integer expansionId,
                               String transactionId) {
        try {
            byte[] salt = KeyDerivationUtil.generateSalt();
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
            String encryptedMessage = EncryptionUtil.encrypt(password, derivedKey);

            integratorClient.changePasswordGame(host, jwt, accountId, userId, encryptedMessage, salt, expansionId,
                    transactionId);
        } catch (Exception e) {
            LOGGER.error("Failed to update game account password");
            throw new InternalException("Failed to update game account password", transactionId);
        }

    }

    @Override
    public List<CharacterProfessionsDto> professions(String host, String jwt, Long accountId, Long characterId,
                                                     String transactionId) {
        return integratorClient.professions(host, jwt, accountId, characterId, transactionId)
                .stream().map(data -> new CharacterProfessionsDto(data.id(), data.logo(), data.name(), data.value(),
                        data.max())).toList();
    }

    @Override
    public void sendMoney(String host, String jwt, Long accountId, Long userId, Long characterId, Long friendId,
                          Long money, Double cost, String transactionId) {

        integratorClient.sendMoney(host, jwt, new SendMoneyRequest(characterId, friendId, accountId, userId, money,
                cost), transactionId);
    }

    @Override
    public void sendLevel(String host, String jwt, Long accountId, Long userId, Long characterId, Long friendId,
                          Integer level, Double cost, String transactionId) {

        integratorClient.sendLevel(host, jwt, new SendLevelRequest(characterId, friendId, accountId, userId, level,
                cost), transactionId);
    }

    @Override
    public CharactersDto loanApplicationCharacters(String host, String jwt, Long accountId, Long userId,
                                                   String transactionId) {
        CharactersResponse response = integratorClient.loanApplicationCharacters(host, jwt, accountId, transactionId);

        if (response == null) {
            LOGGER.error("[IntegratorService] [loanApplicationCharacters] It was not possible to get the characters " +
                            "from the realm.  Host: {} userId: {}",
                    host, userId);
            throw new InternalException("It was not possible to get the characters from the realm.", transactionId);
        }

        CharactersDto characters = new CharactersDto();
        characters.setCharacters(response.getCharacters());
        characters.setTotalQuantity(response.getTotalQuantity());

        return characters;
    }

    @Override
    public GuildsDto guilds(String serverName, Long serverId, String host, String jwt, int size, int page,
                            String search,
                            String transactionId) {

        GuildsResponse response = integratorClient.guilds(host, jwt, size, page, search, transactionId);

        if (response == null) {
            LOGGER.error("[IntegratorService] [guilds] It was not possible to obtain the guilds associated with the " +
                            "realm.  Host: {} transactionId{}",
                    host, transactionId);
            throw new InternalException("It was not possible to obtain the guilds associated with the realm.",
                    transactionId);
        }

        List<GuildDto> guilds =
                response.getGuilds().stream().filter(GuildModel::isPublicAccess).map(guildModel -> new GuildDto(guildModel, serverName, serverId)).toList();

        if (guilds.isEmpty()) {
            return new GuildsDto(guilds, 0L);
        }
        return new GuildsDto(guilds, response.getSize());
    }

    @Override
    public GuildDto guild(String serverName, Long serverId, String host, String jwt, Long guid, String transactionId) {

        GuildResponse response = integratorClient.guild(host, jwt, guid, transactionId);

        if (response == null) {
            LOGGER.error("[IntegratorService] [guild] It was not possible to obtain the guilds associated with the " +
                            "realm.  Host: {} transactionId{}",
                    host, transactionId);
            throw new InternalException("It was not possible to obtain the guilds associated with the realm.",
                    transactionId);
        }

        return new GuildDto(response, serverName, serverId);
    }

    @Override
    public void attachGuild(String host, String jwt, Long accountId, Long guildId, Long characterId,
                            String transactionId) {
        integratorClient.attachGuild(host, jwt, guildId, accountId, characterId, transactionId);
    }

    @Override
    public void unInviteGuild(String host, String jwt, Long userId, Long accountId, Long characterId,
                              String transactionId) {
        integratorClient.unInviteGuild(host, jwt, userId, accountId, characterId, transactionId);
    }

    @Override
    public GuildDetailMemberResponse guildMember(String host, String jwt, Long userId, Long accountId, Long characterId,
                                                 String transactionId) {
        GuildDetailMemberResponse response = integratorClient.guildMember(host, jwt, userId, accountId, characterId,
                transactionId);

        if (response == null) {
            throw new InternalException("guildmember error", transactionId);
        }

        return response;
    }

    @Override
    public void executeCommand(String host, String jwt, String message, byte[] salt, String transactionId) {
        integratorClient.sendCommand(host, jwt, message, salt, transactionId);
    }

    @Override
    public Double collectGold(String host, String jwt, Long userId, Double moneyToPay, String transactionId) {
        return Optional.ofNullable(integratorClient.collectGold(host, jwt, userId, moneyToPay, transactionId))
                .orElse(moneyToPay);
    }

    @Override
    public void purchase(String host, String jwt, Long userId, Long accountId, String reference,
                         List<ItemQuantityModel> items, Double amount,
                         String transactionId) {
        integratorClient.purchase(host, jwt, userId, accountId, reference, items, amount, transactionId);
    }

    @Override
    public void updateGuild(String host, String jwt, Long characterId, Long accountId, boolean isPublic,
                            boolean multiFaction, String discord, String transactionId) {
        integratorClient.updateGuild(host, jwt, characterId, accountId, isPublic, multiFaction, discord, transactionId);
    }

    @Override
    public void sendAnnouncement(String host, String jwt, Long userId, Long accountId, Long characterId, Long skillId,
                                 String message, String transactionId) {

        integratorClient.sendAnnouncement(host, jwt, new AnnouncementRequest(accountId, characterId, skillId, userId,
                message), transactionId);
    }

    @Override
    public void sendBenefitsPremium(String host, String jwt, Long userId, Long accountId, Long characterId,
                                    List<ItemQuantityModel> items, String benefitType, Double amount,
                                    String transactionId) {
        integratorClient.sendBenefitsPremium(host, jwt, new SubscriptionBenefitsRequest(userId, accountId,
                characterId, items, benefitType, amount), transactionId);
    }

    @Override
    public void sendPromo(String host, String jwt, Long userId, Long accountId, Long characterId,
                          List<ItemQuantityModel> items, String type, Double amount, Integer minLvl, Integer maxLvl,
                          Integer level,
                          String transactionId) {
        integratorClient.sendPromo(host, jwt, new ClaimPromoRequest(userId, accountId, characterId, items, type,
                amount, minLvl, maxLvl, level), transactionId);
    }

    @Override
    public void sendGuildBenefit(String host, String jwt, Long userId, Long accountId, Long characterId,
                                 List<ItemQuantityModel> items, String transactionId) {
        integratorClient.sendGuildBenefits(host, jwt, new BenefitsGuildRequest(userId, accountId, characterId, items)
                , transactionId);
    }

    @Override
    public ClaimMachineResponse claimMachine(String host, String jwt, Long userId, Long accountId, Long characterId,
                                             String type, String transactionId) {
        GenericResponse<ClaimMachineResponse> response = integratorClient.claimMachine(host, jwt,
                new ClaimMachineRequest(userId, accountId, characterId, type), transactionId);

        if (response == null) {
            throw new InternalException("Something is wrong with the roulette system, please contact support",
                    transactionId);
        }

        return response.getData();
    }

    @Override
    public AccountsResponse accountsServer(String host, String jwt, int size, int page, String filter,
                                           String transactionId) {

        GenericResponse<AccountsResponse> response = integratorClient.accountsServer(host, jwt,
                size, page, filter, transactionId);

        if (response == null) {
            throw new InternalException("Could not get realm accounts",
                    transactionId);
        }

        return response.getData();
    }

    @Override
    public DashboardMetricsResponse dashboard(String host, String jwt, String transactionId) {
        return integratorClient.metricsDashboard(host, jwt, transactionId).getData();
    }

    @Override
    public void updateMailAccount(String host, String jwt, AccountUpdateMailRequest request, String transactionId) {
        integratorClient.updateMail(host, jwt, request, transactionId);
    }

    @Override
    public List<CharacterInventoryResponse> getCharacterInventory(String host, String jwt, Long characterId,
                                                                  Long accountId
            , String transactionId) {

        return integratorClient.getCharacterInventory(host, jwt, characterId, accountId, transactionId).getData();
    }

    @Override
    public void transferInventoryItem(String host, String jwt, Long accountId, Long characterId,
                                      Long friendId, Integer quantity, Long itemId, String transactionId) {

        integratorClient.transferInventoryItem(host, jwt, new TransferInventoryRequest(characterId, friendId, itemId,
                quantity, accountId), transactionId);
    }

    @Override
    public void bannedUser(String host, String jwt, String username, Integer days, Integer hours, Integer minutes,
                           Integer seconds, String bannedBy, String banReason, String transactionId) {
        integratorClient.banAccount(host, jwt, new AccountBanRequest(username, days, hours, minutes, seconds,
                bannedBy, banReason), transactionId);
    }

    @Override
    public Map<String, String> getConfigs(String host, String jwt, String url, boolean authServer,
                                          String transactionId) {
        return integratorClient.emulatorConfiguration(host, jwt, new EmulatorConfigRequest(url),
                transactionId).getData();
    }

    @Override
    public void teleport(String host, String jwt, TeleportRequest request, String transactionId) {
        integratorClient.teleport(host, jwt, request, transactionId);
    }

}
