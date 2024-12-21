package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.*;

@Data
public class AccountGameResponse {
    private Long id;
    private String username;
    private String email;
    private String expansion;
    private boolean online;
    @JsonProperty("failed_logins")
    private String failedLogins;
    @JsonProperty("join_date")
    private LocalDate joinDate;
    @JsonProperty("last_ip")
    private String lastIp;
    @JsonProperty("mute_reason")
    private String muteReason;
    @JsonProperty("mute_by")
    private String muteBy;
    private boolean mute;
    @JsonProperty("last_login")
    private LocalDate lastLogin;
    private String os;
}
