package com.register.wowlibre.domain.dto.teleport;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class TeleportDto {
    @NotNull
    private String imgUrl;
    @NotNull
    private String name;
    @NotNull
    private Double positionX;
    @NotNull
    private Double positionY;
    @NotNull
    private Double positionZ;
    @NotNull
    private Integer map;
    @NotNull
    private Double orientation;
    @NotNull
    private Integer zone;
    @NotNull
    private Double area;
    @NotNull
    private String faction;
    @NotNull
    private Long realmId;
}
