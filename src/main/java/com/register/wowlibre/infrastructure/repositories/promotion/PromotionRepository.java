package com.register.wowlibre.infrastructure.repositories.promotion;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface PromotionRepository extends CrudRepository<PromotionEntity, Long> {

    List<PromotionEntity> findByServerIdAndLanguageAndStatusIsTrue(Long serverId, String language);

    List<PromotionEntity> findByServerIdAndStatusIsTrue(Long serverId);

    Optional<PromotionEntity> findByIdAndServerIdAndLanguageAndStatusIsTrue(Long Id, Long serverId, String language);
}
