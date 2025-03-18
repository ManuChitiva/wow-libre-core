package com.register.wowlibre.domain.port.out.promotion;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainPromotion {

    List<PromotionEntity> findByPromotionServerIdAndLanguage(Long serverId, String language);

    Optional<PromotionEntity> findByIdAndServerIdAndLanguage(Long id, Long serverId, String language);

    List<PromotionEntity> findByPromotionServerId(Long serverId);


}
