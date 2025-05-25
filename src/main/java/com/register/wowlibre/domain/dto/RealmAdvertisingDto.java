package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RealmAdvertisingDto {
    @NotNull
    private String title;
    @NotNull
    private String tag;
    @NotNull
    private String subTitle;
    @NotNull
    private String description;
    @NotNull
    private String ctaPrimary;
    @NotNull
    private String imgUrl;
    @NotNull
    private String footerDisclaimer;
    @NotNull
    private String language;
}
