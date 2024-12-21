package com.register.wowlibre.domain.enums;

import com.register.wowlibre.domain.exception.*;
import lombok.*;

import java.util.*;

@Getter
public enum ServerServices {
    BANK("BANK");

    private final String name;

    ServerServices(String name) {
        this.name = name;
    }

    public static ServerServices getName(String name, String transactionId) {
        return Arrays.stream(values())
                .filter(data -> Objects.equals(data.name, name))
                .findFirst()
                .orElseThrow(() -> new InternalException("The server service with name " + name + " was not found.",
                        transactionId));
    }
}
