package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import com.register.wowlibre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class BenefitsGuildRequest {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("character_id")
    private Long characterId;

    private List<ItemQuantityModel> items;
}
