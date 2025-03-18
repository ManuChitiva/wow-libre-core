package com.register.wowlibre.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AccountBanDto {
    @NotNull(message = "The username is required")
    private String username;
    @NotNull
    private Integer days;
    @NotNull
    private Integer hours;
    @NotNull
    private Integer minutes;
    @NotNull
    private Integer seconds;
    @NotNull
    private String bannedBy;
    @NotNull
    private String banReason;
    @NotNull
    private Long serverId;
    @NotNull(message = "The password is required")
    private String password;
}
