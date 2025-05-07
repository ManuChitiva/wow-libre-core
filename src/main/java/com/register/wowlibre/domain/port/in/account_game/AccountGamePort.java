package com.register.wowlibre.domain.port.in.account_game;

import com.register.wowlibre.domain.dto.*;

public interface AccountGamePort {
    AccountsDto accounts(Long userId, int page, int size, String searchUsername, String serverName,
                         String transactionId);

    void create(Long userId, String serverName, Integer expansionId, String username, String password,
                String transactionId);

    AccountsDto accounts(Long userId, Long serverId, String transactionId);

    AccountVerificationDto verifyAccount(Long userId, Long accountId, Long serverId, String transactionId);

    AccountDetailDto account(Long userId, Long accountId, Long serverId, String transactionId);


}
