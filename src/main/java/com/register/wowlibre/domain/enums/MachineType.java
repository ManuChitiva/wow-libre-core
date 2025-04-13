package com.register.wowlibre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
public enum MachineType {
    ITEMS("Item", "World of Warcraft Clásico", "https://i.ibb.co/tCwJwz5/classic-wow-libre-expansion.png"),
    LEVEL("Level", "World of Warcraft Clásico", "https://i.ibb.co/tCwJwz5/classic-wow-libre-expansion.png"),
    MOUNT("Mount", "World of Warcraft Clásico", "https://i.ibb.co/tCwJwz5/classic-wow-libre-expansion.png"),
    GOLD("Gold", "World of Warcraft Clásico", "https://i.ibb.co/tCwJwz5/classic-wow-libre-expansion.png");

    private final String name;
    private final String message;
    private final String logo;

    MachineType(String name, String message, String logo) {
        this.name = name;
        this.message = message;
        this.logo = logo;
    }

    public static MachineType getName(String name) {
        return Arrays.stream(values())
                .filter(data -> Objects.equals(data.name, name))
                .findFirst()
                .orElse(null);
    }
}
