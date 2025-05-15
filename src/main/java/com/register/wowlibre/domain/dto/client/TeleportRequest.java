package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Builder
public class TeleportRequest {
    @JsonProperty("position_x")
    public final Double positionX;
    @JsonProperty("position_y")
    public final Double positionY;
    @JsonProperty("position_z")
    public final Double positionZ;
    public final Integer map;
    public final Double orientation;
    public final Integer zone;
    @JsonProperty("character_id")
    public final Long characterId;
    @JsonProperty("account_id")
    public final Long accountId;
    @JsonProperty("user_id")
    public final Long userId;
}
