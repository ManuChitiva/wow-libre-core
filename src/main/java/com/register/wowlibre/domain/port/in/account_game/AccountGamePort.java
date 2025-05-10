package com.register.wowlibre.domain.port.in.account_game;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.account_game.AccountsGameDto;

public interface AccountGamePort {

    void create(Long userId, String serverName, Integer expansionId, String username, String password,
                String transactionId);

    AccountsGameDto accounts(Long userId, int page, int size, String searchUsername, String serverName,
                             String transactionId);
    AccountsGameDto accounts(Long userId, Long realmId, String transactionId);

    AccountVerificationDto verifyAccount(Long userId, Long accountId, Long realmId, String transactionId);

    AccountGameDetailDto account(Long userId, Long accountId, Long realmId, String transactionId);

}
