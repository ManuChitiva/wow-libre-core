package com.register.wowlibre.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class EmulatorConfigDto {
    private String route;
    @JsonProperty("auth_server")
    private boolean authServer;
    @JsonProperty("server_id")
    private Long serverId;
}
