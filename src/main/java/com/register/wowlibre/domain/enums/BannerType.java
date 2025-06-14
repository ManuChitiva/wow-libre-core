package com.register.wowlibre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
public enum BannerType {
    IMAGE,
    VIDEO;

    public static BannerType fromName(String name) {
        return Arrays.stream(BannerType.values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
