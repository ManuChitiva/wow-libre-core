package com.register.wowlibre.infrastructure.util;

import lombok.*;

@Getter
public enum Rol {
    ADMIN("ADMIN"),
    CLIENT("CLIENT"),
    SERVER("SERVER");

    private final String name;

    Rol(String name) {
        this.name = name;
    }

}
