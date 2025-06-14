package com.register.wowlibre.domain.port.out.banners;

import com.register.wowlibre.infrastructure.entities.*;

public interface DeleteBanners {
    void delete(BannersEntity banners);
}
