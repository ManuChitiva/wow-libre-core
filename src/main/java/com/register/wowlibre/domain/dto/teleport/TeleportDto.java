package com.register.wowlibre.domain.dto.teleport;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class TeleportDto {
    @NotNull
    @Length(min = 5, max = 200)
    private String imgUrl;
    @NotNull
    @Length(min = 5, max = 50)
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
    @Length(min = 5, max = 10)
    private String faction;
    @NotNull
    private Long realmId;
}
