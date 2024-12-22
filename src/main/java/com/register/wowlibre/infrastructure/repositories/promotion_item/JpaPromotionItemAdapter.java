package com.register.wowlibre.infrastructure.repositories.promotion_item;

import com.register.wowlibre.domain.port.out.promotion_item.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaPromotionItemAdapter implements SavePromotionItem, ObtainPromotionItem {
    private final PromotionItemRepository promotionItemRepository;

    public JpaPromotionItemAdapter(PromotionItemRepository promotionItemRepository) {
        this.promotionItemRepository = promotionItemRepository;
    }

    @Override
    public void save(PromotionItemEntity promotionItem, String transactionId) {
        promotionItemRepository.save(promotionItem);
    }

    @Override
    public List<PromotionItemEntity> findByPromotionId(PromotionEntity promotion, String transactionId) {
        return promotionItemRepository.findByPromotionId(promotion);
    }
}
