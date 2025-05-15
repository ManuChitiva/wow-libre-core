package com.register.wowlibre.domain.enums;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public enum WowFactionRace {
    HUMAN(1, Faction.ALLIANCE),
    ORC(2, Faction.HORDE),
    DWARF(3, Faction.ALLIANCE),
    NIGHT_ELF(4, Faction.ALLIANCE),
    UNDEAD(5, Faction.HORDE),
    TAUREN(6, Faction.HORDE),
    GNOME(7, Faction.ALLIANCE),
    TROLL(8, Faction.HORDE),
    GOBLIN(9, Faction.HORDE),
    BLOOD_ELF(10, Faction.HORDE),
    DRAENEI(11, Faction.ALLIANCE),
    WORGEN(22, Faction.ALLIANCE),
    PANDAREN_ALLIANCE(24, Faction.ALLIANCE),
    PANDAREN_HORDE(25, Faction.HORDE),
    NIGHTBORNE(27, Faction.HORDE),
    HIGHMOUNTAIN_TAUREN(28, Faction.HORDE),
    VOID_ELF(29, Faction.ALLIANCE),
    LIGHTFORGED_DRAENEI(30, Faction.ALLIANCE),
    DARK_IRON_DWARF(34, Faction.ALLIANCE),
    VULPERA(35, Faction.HORDE),
    MAGHAR_ORC(36, Faction.HORDE),
    MECHAGNOME(37, Faction.ALLIANCE),
    UNKNOWN(0, Faction.ALL);

    private final int id;
    private final Faction faction;

    public static WowFactionRace getById(Long id) {
        return Arrays.stream(values())
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
