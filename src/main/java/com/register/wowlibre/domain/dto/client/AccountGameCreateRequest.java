package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Builder
public class AccountGameCreateRequest {
    public String username;
    public String password;
    public String email;
    @JsonProperty("user_id")
    public Long userId;
    public Integer expansionId;
    public byte[] salt;
}
