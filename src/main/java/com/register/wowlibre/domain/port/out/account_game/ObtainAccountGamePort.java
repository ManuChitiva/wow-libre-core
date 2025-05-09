package com.register.wowlibre.domain.port.out.account_game;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainAccountGamePort {
    List<AccountGameEntity> findByUserIdAndStatusIsTrue(Long userId, int page, int size, String transactionId);

    Optional<AccountGameEntity> findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(Long userId, Long accountId, Long serverId,
                                                                                  String transactionId);

    Long accounts(Long userId);

    List<AccountGameEntity> findByUserIdAndServerNameAndUsernameStatusIsTrue(Long userId, int page, int size,
                                                                             String serverName,
                                                                             String username, String transactionId);

    List<AccountGameEntity> findByUserIdAndServerId(Long userId, Long serverId, String transactionId);


}
