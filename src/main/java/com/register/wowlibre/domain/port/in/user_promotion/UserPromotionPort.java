package com.register.wowlibre.domain.port.in.user_promotion;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface UserPromotionPort {
    void save(Long userId, Long accountId, Long promotionId, Long characterId, String transactionId);

    Optional<UserPromotionEntity> findByUserIdAndAccountIdAndPromotionIdAndCharacterId(Long userId, Long accountId, Long promotionId,
                                                           Long characterId,
                                                           String transactionId);
}
