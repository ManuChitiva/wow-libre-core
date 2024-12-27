package com.register.wowlibre.application.services.user_promotion;

import com.register.wowlibre.domain.port.in.user_promotion.*;
import com.register.wowlibre.domain.port.out.user_promotion.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserPromotionService implements UserPromotionPort {

    private final ObtainUserPromotion obtainUserPromotion;
    private final SaveUserPromotion saveUserPromotion;

    public UserPromotionService(ObtainUserPromotion obtainUserPromotion, SaveUserPromotion saveUserPromotion) {
        this.obtainUserPromotion = obtainUserPromotion;
        this.saveUserPromotion = saveUserPromotion;
    }

    @Override
    public void save(Long userId, Long accountId, Long promotionId, Long characterId, String transactionId) {
        UserPromotionEntity userPromotionEntity = new UserPromotionEntity();
        userPromotionEntity.setUserId(userId);
        userPromotionEntity.setCreatedAt(new Date());
        userPromotionEntity.setPromotionId(promotionId);
        userPromotionEntity.setCharacterId(characterId);
        userPromotionEntity.setAccountId(accountId);

        saveUserPromotion.save(userPromotionEntity, transactionId);
    }

    @Override
    public Optional<UserPromotionEntity> findByUserIdAndAccountIdAndPromotionIdAndCharacterId(Long userId, Long accountId, Long promotionId,
                                                                  Long characterId,
                                                                  String transactionId) {
        return obtainUserPromotion.findByUserIdAndAccountIdAndPromotionIdAndCharacterId(userId, accountId, promotionId, characterId, transactionId);
    }
}
