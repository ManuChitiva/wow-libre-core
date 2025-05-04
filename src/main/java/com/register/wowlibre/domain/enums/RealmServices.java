package com.register.wowlibre.domain.enums;

import com.register.wowlibre.domain.exception.*;
import lombok.*;

import java.util.*;

@Getter
public enum RealmServices {
    BANK("BANK"),
    SEND_LEVEL("SEND_LEVEL");

    private final String name;

    RealmServices(String name) {
        this.name = name;
    }

    public static RealmServices getName(String name, String transactionId) {
        return Arrays.stream(values())
                .filter(data -> Objects.equals(data.name, name))
                .findFirst()
                .orElseThrow(() -> new InternalException("The realm service with name " + name + " was not found.",
                        transactionId));
    }
}
