package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class BannerDto {
    @NotNull
    private String mediaUrl;
    @NotNull
    private String alt;
    @NotNull
    private String language;
    @NotNull
    private String type;
    private String label;
}
