package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@Getter
public class VerifyCaptchaRequest {
    private String secret;
    private String response;
    @JsonProperty("remoteip")
    private String remoteip;
}
