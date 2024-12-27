package com.register.wowlibre.domain.dto.client;

import lombok.*;

@Data
public class VerifyCaptchaResponse {
    private Boolean success;
    private String hostname;
}
