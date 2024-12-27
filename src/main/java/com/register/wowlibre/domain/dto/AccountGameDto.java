package com.register.wowlibre.domain.dto;

import lombok.*;

import java.time.*;

@Data
@AllArgsConstructor
public class AccountGameDto {
    private Long id;
    private String username;
    private String email;
    private String expansion;
    private boolean online;
    private String failedLogins;
    private LocalDate joinDate;
    private String lastIp;
    private String muteReason;
    private String muteBy;
    private boolean mute;
    private LocalDate lastLogin;
    private String os;
}
