package com.register.wowlibre.domain.enums;

import lombok.*;

import java.util.*;
@Getter
public enum Expansion {
    WOW_CLASSIC(0, "World of Warcraft Clásico", "https://i.ibb.co/tCwJwz5/classic-wow-libre-expansion.png"),
    THE_BURNING_CRUSADE(1, "The Burning Crusade", "https://i.ibb.co/zf2pqNZ/Burning-crusade-wow-libre-expansion.png"),
    WRATH_OF_THE_LICH_KING(2, "Wrath of the Lich King", "https://i.ibb.co/4jg39fc/lichking-wow-libre-expansion.png"),
    CATACLYSM(3, "Cataclysm", "https://i.ibb.co/0G7bRpg/cataclysm-wow-libre-expansion.png"),
    MISTS_OF_PANDARIA(4, "Mists of Pandaria", "https://i.ibb.co/cDY8BRs/Mistsof-Pandariawow-libre-expansion.png"),
    WARLORDS_OF_DRAENOR(5, "Warlords of Draenor", "https://i.ibb.co/Ws6nvm0/warlods-of-draenor-wow-libre-expansion" +
                                ".png"),
    LEGION(6, "Legion", "https://i.ibb.co/DY7BQpt/legionn-wow-libre-expansion.png"),
    BATTLE_FOR_AZEROTH(7, "Battle for Azeroth", "https://i.ibb.co/GMqShXd/battle-For-Azeroth-wow-libre-expansion.png"),
    SHADOWLANDS(8, "Shadowlands", "https://i.ibb.co/JvpHqj6/SHADOWLANDS-wow-libre-expansion.png");

    private final int value;
    private final String name;
    private final String logo;

    Expansion(int value, String name, String logo) {
        this.value = value;
        this.name = name;
        this.logo = logo;
    }

    public static Expansion getById(int id) {
        return Arrays.stream(values())
                .filter(expansion -> expansion.value == id)
                .findFirst()
                .orElse(null);
    }
}
