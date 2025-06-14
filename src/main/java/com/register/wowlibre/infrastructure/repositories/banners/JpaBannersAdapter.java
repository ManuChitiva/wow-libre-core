package com.register.wowlibre.infrastructure.repositories.banners;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.port.out.banners.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaBannersAdapter implements ObtainBanners, SaveBanners, DeleteBanners {
    private final BannersRepository bannersRepository;

    public JpaBannersAdapter(BannersRepository bannersRepository) {
        this.bannersRepository = bannersRepository;
    }

    @Override
    public List<BannersEntity> findByLanguage(String language) {
        return bannersRepository.findByLanguage(language);
    }

    @Override
    public Optional<BannersEntity> findById(Long id) {
        return bannersRepository.findById(id);
    }

    @Override
    public boolean isValidBanner(String language, BannerType type, String transactionId) {

        List<BannersEntity> banners = findByLanguage(language);

        if (banners.isEmpty()) {
            return true;
        }

        return Optional.of(banners)
                .filter(list -> list.size() < 5)
                .map(list -> list.stream().allMatch(banner -> banner.getType().equals(type)))
                .orElse(false);
    }

    @Override
    public void save(BannersEntity bannersEntity) {
        bannersRepository.save(bannersEntity);
    }

    @Override
    public void delete(BannersEntity banners) {
        bannersRepository.delete(banners);
    }
}
