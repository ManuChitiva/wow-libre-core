package com.register.wowlibre.domain.port.out.promotion;

import com.register.wowlibre.infrastructure.entities.*;

public interface SavePromotion {
    void save(PromotionEntity promotion);
}
