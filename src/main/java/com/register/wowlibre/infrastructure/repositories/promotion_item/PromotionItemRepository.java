package com.register.wowlibre.infrastructure.repositories.promotion_item;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PromotionItemRepository extends CrudRepository<PromotionItemEntity, Long> {

    List<PromotionItemEntity> findByPromotionId(PromotionEntity promotion);
}
