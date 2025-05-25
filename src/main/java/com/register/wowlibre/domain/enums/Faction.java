package com.register.wowlibre.domain.enums;

import lombok.*;

@Getter
public enum Faction {
    HORDE,
    ALLIANCE,
    ALL;

    public static Faction fromString(String name) {
        if (name == null || name.isBlank()) {
            return ALL;
        }
        try {
            return Faction.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ALL;
        }
    }
}
