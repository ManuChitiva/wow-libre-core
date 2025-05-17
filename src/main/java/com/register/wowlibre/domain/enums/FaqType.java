package com.register.wowlibre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
public enum FaqType {
    SUPPORT,
    SUBSCRIPTION;
    public static FaqType getByName(String name) {
        return Arrays.stream(values())
                .filter(data -> Objects.equals(data.name(), name))
                .findFirst()
                .orElse(null);
    }
}
