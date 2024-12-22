package com.register.wowlibre.infrastructure.repositories.promotion;

import com.register.wowlibre.domain.port.out.promotion.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPromotionAdapter implements SavePromotion, ObtainPromotion {
    private final PromotionRepository promotionRepository;

    public JpaPromotionAdapter(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }


    @Override
    public List<PromotionEntity> findByPromotionServerIdAndLanguage(Long serverId, String language) {
        return promotionRepository.findByServerIdAndLanguage(serverId, language);
    }

    @Override
    public void save(PromotionEntity promotion) {
        promotionRepository.save(promotion);
    }
}
