package com.register.wowlibre.domain.port.in.characters;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;

import java.util.*;

public interface CharactersPort {
    CharactersDto characters(Long userId, Long accountId, Long serverId, String transactionId);

    CharactersDto loanApplicationCharacters(Long userId, Long accountId, Long serverId, String transactionId);

    void deleteFriend(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                      String transactionId);

    MailsDto mails(Long userId, Long accountId, Long serverId, Long characterId, String transactionId);

    CharacterSocialDto friends(Long userId, Long accountId, Long serverId, Long characterId, String transactionId);

    void changePassword(Long userId, Long accountId, Long serverId, String password, String newPassword,
                        String transactionId);

    List<CharacterProfessionsDto> professions(Long userId, Long accountId, Long serverId, Long characterId,
                                              String transactionId);

    void sendLevel(Long userId, Long accountId, Long serverId, Long characterId, Long friendId, Integer level,
                   String transactionId);

    void sendMoney(Long userId, Long accountId, Long serverId, Long characterId, Long friendId, Long money,
                   String transactionId);

    void sendAnnouncement(Long userId, Long accountId, Long serverId, Long characterId, Long skillId, String message,
                          String transactionId);

    List<CharacterInventoryResponse> getCharacterInventory(Long userId, Long accountId, Long serverId, Long characterId,
                                                           String transactionId);

    void transferInventoryItem(Long userId, Long accountId, Long serverId, Long characterId, Long friendId,
                               Integer count, Long itemId, String transactionId);
}
