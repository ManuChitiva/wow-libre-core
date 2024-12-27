package com.register.wowlibre.infrastructure.repositories.user_promotion;

import com.register.wowlibre.domain.port.out.user_promotion.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaUserPromotionAdapter implements SaveUserPromotion, ObtainUserPromotion {

    private final UserPromotionRepository userPromotionRepository;

    public JpaUserPromotionAdapter(UserPromotionRepository userPromotionRepository) {
        this.userPromotionRepository = userPromotionRepository;
    }

    @Override
    public Optional<UserPromotionEntity> findByUserIdAndAccountIdAndPromotionIdAndCharacterId(Long userId, Long accountId, Long promotionId,
                                                                  Long characterId,
                                                                  String transactionId) {
        return userPromotionRepository.findByUserIdAndAccountIdAndPromotionIdAndCharacterId(userId, accountId,
                promotionId, characterId);
    }

    @Override
    public void save(UserPromotionEntity userPromotionEntity, String transactionId) {
        userPromotionRepository.save(userPromotionEntity);
    }
}
