package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@Getter
public class AuthClientCreateRequest {
    private String username;
    private String password;
    private byte[] salt;
    @JsonProperty("api_key")
    private String apiKey;
    private String emulator;
    @JsonProperty("expansion_id")
    private Integer expansionId;
    @JsonProperty("gm_username")
    private String gmUsername;
    @JsonProperty("gm_password")
    private String gmPassword;
}
