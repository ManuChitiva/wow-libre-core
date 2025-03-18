package com.register.wowlibre.domain.port.in.integrator;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface IntegratorPort {
    Long create(String host, String apiSecret, String expansion, String username, String password,
                String email, Long userId, String transactionId);

    CharactersDto characters(String host, String jwt, Long accountId, Long userId, String transactionId);

    AccountDetailResponse account(String host, String jwt, Long accountId, String transactionId);

    MailsDto mails(String host, String jwt, Long characterId, String transactionId);

    void deleteFriend(String host, String jwt, Long characterId, Long friendId, Long accountId, Long userId,
                      String transactionId);

    CharacterSocialDto friends(String host, String jwt, Long characterId, String transactionId);

    void changePassword(String host, String apiSecret, String jwt, Long accountId, Long userId, String password,
                        String transactionId);

    List<CharacterProfessionsDto> professions(String host, String jwt, Long accountId, Long characterId,
                                              String transactionId);

    void sendMoney(String host, String jwt, Long accountId, Long userId, Long characterId, Long friendId, Long money,
                   Double cost, String transactionId);

    void sendLevel(String host, String jwt, Long accountId, Long userId, Long characterId, Long friendId,
                   Integer level, Double cost, String transactionId);

    CharactersDto loanApplicationCharacters(String host, String jwt, Long accountId, Long userId, String transactionId);


    GuildsDto guilds(String serverName, Long serverId, String host, String jwt, int size, int page, String search,
                     String transactionId);

    GuildDto guild(String serverName, Long serverId, String host, String jwt, Long guid, String transactionId);

    void attachGuild(String host, String jwt, Long accountId, Long guildId, Long characterId, String transactionId);


    void unInviteGuild(String host, String jwt, Long userId, Long accountId, Long characterId, String transactionId);

    GuildDetailMemberResponse guildMember(String host, String jwt, Long userId, Long accountId, Long characterId,
                                          String transactionId);

    void executeCommand(String host, String jwt, String message, byte[] salt, String transactionId);

    Double collectGold(String host, String jwt, Long userId, Double moneyToPay, String transactionId);

    void purchase(String host, String jwt, Long userId, Long accountId, String reference,
                  List<ItemQuantityModel> items, Double amount,
                  String transactionId);

    void updateGuild(String host, String jwt, Long characterId, Long accountId, boolean isPublic,
                     boolean multiFaction, String discord, String transactionId);


    void sendAnnouncement(String host, String jwt, Long userId, Long accountId, Long characterId, Long skillId,
                          String transactionId);

    void sendBenefitsPremium(String host, String jwt, Long userId, Long accountId,
                             Long characterId, List<ItemQuantityModel> items,
                             String benefitType, Double amount, String transactionId);

    void sendPromo(String host, String jwt, Long userId, Long accountId,
                   Long characterId, List<ItemQuantityModel> items,
                   String type, Double amount, Integer minLvl, Integer maxLvl, Integer level, String transactionId);

    void sendGuildBenefit(String host, String jwt, Long userId, Long accountId, Long characterId,
                          List<ItemQuantityModel> items, String transactionId);

    ClaimMachineResponse claimMachine(String host, String jwt, Long userId, Long accountId, Long characterId,
                                      String type, String transactionId);

    AccountsResponse accountsServer(String host, String jwt, int size, int page, String filter,
                                    String transactionId);

    DashboardMetricsResponse dashboard(String host, String jwt, String transactionId);

    void updateMailAccount(String host, String jwt, AccountUpdateMailRequest request, String transactionId);

    List<CharacterInventoryResponse> getCharacterInventory(String host, String jwt, Long characterId,
                                                           Long accountId, String transactionId);

    void transferInventoryItem(String host, String jwt, Long accountId, Long characterId, Long friendId,
                               Integer count, Long itemId, String transactionId);

    void bannedUser(String host, String jwt, String username, Integer days, Integer hours, Integer minutes,
                    Integer seconds, String bannedBy, String banReason, String transactionId);
}
