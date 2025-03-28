package com.register.wowlibre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Data
public class AnnouncementDto {
    @NotNull
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("character_id")
    @NotNull
    private Long characterId;
    @NotNull
    @JsonProperty("skill_id")
    private Long skillId;
    @NotNull
    private String message;
    @NotNull
    private Long serverId;
}
