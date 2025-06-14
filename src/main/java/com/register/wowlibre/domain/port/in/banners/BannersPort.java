package com.register.wowlibre.domain.port.in.banners;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface BannersPort {
    List<BannerModel> findByLanguage(String language);

    void saveBanner(BannerDto bannerModel, String transactionId);

    void deleteBanner(Long bannerId, String transactionId);
}
