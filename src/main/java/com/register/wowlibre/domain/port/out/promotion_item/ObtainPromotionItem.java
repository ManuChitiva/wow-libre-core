package com.register.wowlibre.domain.port.out.promotion_item;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPromotionItem {
    List<PromotionItemEntity> findByPromotionId(PromotionEntity promotion, String transactionId);
}
