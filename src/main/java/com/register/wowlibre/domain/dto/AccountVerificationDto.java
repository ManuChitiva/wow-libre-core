package com.register.wowlibre.domain.dto;

import com.register.wowlibre.infrastructure.entities.*;

import java.io.*;


public record AccountVerificationDto(RealmEntity server, AccountGameEntity accountGame) implements Serializable {
}
