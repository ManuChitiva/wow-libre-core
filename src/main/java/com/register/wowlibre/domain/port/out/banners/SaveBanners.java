package com.register.wowlibre.domain.port.out.banners;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveBanners {
    void save(BannersEntity bannersEntity);
}
