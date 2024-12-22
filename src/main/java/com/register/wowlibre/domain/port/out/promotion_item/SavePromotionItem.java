package com.register.wowlibre.domain.port.out.promotion_item;

import com.register.wowlibre.infrastructure.entities.*;

public interface SavePromotionItem {

    void save(PromotionItemEntity promotionItem, String transactionId);
}
