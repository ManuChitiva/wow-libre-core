package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Builder
public class AccountGameCreateRequest {
    private String username;
    private String password;
    private String email;
    @JsonProperty("user_id")
    private Long userId;
    private Integer expansionId;
    private byte[] salt;
}
