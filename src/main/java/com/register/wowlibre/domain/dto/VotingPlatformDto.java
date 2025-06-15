package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class VotingPlatformDto {
    @NotNull
    private String name;
    @NotNull
    private String imgUrl;
    @NotNull
    private String postbackUrl;

    private String allowedHost;
}
