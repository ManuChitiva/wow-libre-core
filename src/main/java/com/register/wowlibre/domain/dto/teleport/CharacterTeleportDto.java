package com.register.wowlibre.domain.dto.teleport;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CharacterTeleportDto {
    @NotNull
    private Long teleportId;
    @NotNull
    private Long characterId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long realmId;
}
