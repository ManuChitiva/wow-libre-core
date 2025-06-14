package com.register.wowlibre.domain.port.out.banners;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainBanners {
    List<BannersEntity> findByLanguage(String language);

    Optional<BannersEntity> findById(Long id);

    boolean isValidBanner(String language, BannerType type, String transactionId);
}
