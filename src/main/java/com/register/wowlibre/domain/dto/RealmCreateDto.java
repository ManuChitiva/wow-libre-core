package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class RealmCreateDto {
    @NotNull
    @Length(min = 5, max = 40)
    private String name;
    @NotNull
    @Length(min = 5, max = 40)
    private String emulator;
    @NotNull
    private Integer expansion;
    @NotNull
    @Length(min = 5, max = 50)
    private String webSite;
    @NotNull
    @Length(min = 5, max = 50)
    private String host;
    @NotNull
    @Length(min = 5, max = 30)
    private String password;
    @NotNull
    @Length(min = 5, max = 80)
    private String realmlist;
    @NotNull
    @Length(min = 5, max = 30)
    private String externalUsername;
    @NotNull
    @Length(min = 5, max = 30)
    private String externalPassword;
    @NotNull
    @Length(min = 5, max = 30)
    private String type;
}
