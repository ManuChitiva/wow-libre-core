package com.register.wowlibre.domain.dto.account_game;

import com.register.wowlibre.infrastructure.entities.*;

import java.io.*;


public record AccountVerificationDto(RealmEntity realm, AccountGameEntity accountGame) implements Serializable {
}
