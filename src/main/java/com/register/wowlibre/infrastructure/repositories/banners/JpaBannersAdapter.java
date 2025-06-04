package com.register.wowlibre.infrastructure.repositories.banners;

import org.springframework.stereotype.*;

@Repository
public class JpaBannersAdapter {
    private final BannersRepository bannersRepository;
    public JpaBannersAdapter(BannersRepository bannersRepository) {
        this.bannersRepository = bannersRepository;
    }
}
