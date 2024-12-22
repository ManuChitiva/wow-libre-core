package com.register.wowlibre.domain.port.out.user_promotion;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainUserPromotion {
    Optional<UserPromotionEntity> findByUserIdAndAccountIdAndPromotionIdAndCharacterId(Long userId, Long accountId, Long promotionId,
                                                           Long characterId,
                                                           String transactionId);
}
