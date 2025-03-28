package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@Data
public class AnnouncementRequest {
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
    @JsonProperty("user_id")
    private Long userId;
    private String message;
}
