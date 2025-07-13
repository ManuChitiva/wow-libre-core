package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class RealmAdvertisingDto {
    @NotNull
    @Length(min = 5, max = 10)
    private String tag;
    @NotNull
    @Length(min = 5, max = 40)
    private String subTitle;
    @NotNull
    @Length(min = 5, max = 40)
    private String description;
    @NotNull
    @Length(min = 5, max = 20)
    private String ctaPrimary;
    @NotNull
    private String imgUrl;
    @NotNull
    @Length(min = 5, max = 40)
    private String footerDisclaimer;
    @NotNull
    @Length(min = 2, max = 10)
    private String language;
}
