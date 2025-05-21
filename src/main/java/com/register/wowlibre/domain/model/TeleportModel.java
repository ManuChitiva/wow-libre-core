package com.register.wowlibre.domain.model;

import com.register.wowlibre.domain.enums.*;
import lombok.*;

@Builder
public class TeleportModel {
    public final Long id;
    public final String name;
    public final String imgUrl;
    public final Double positionX;
    public final Double positionY;
    public final Double positionZ;
    public final Integer map;
    public final Double orientation;
    public final Integer zone;
    public final Double area;
    public final Faction faction;
    public final Long realmId;
}
