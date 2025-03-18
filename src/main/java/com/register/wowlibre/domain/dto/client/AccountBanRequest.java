package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@Data
public class AccountBanRequest {
    private String username;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private Integer seconds;
    @JsonProperty("banned_by")
    private String bannedBy;
    @JsonProperty("ban_reason")
    private String banReason;
}
