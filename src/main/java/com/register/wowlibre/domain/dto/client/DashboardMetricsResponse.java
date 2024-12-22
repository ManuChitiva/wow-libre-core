package com.register.wowlibre.domain.dto.client;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DashboardMetricsResponse {
    @JsonProperty("total_users")
    private Long totalUsers;
    @JsonProperty("online_users")
    private Long onlineUsers;
    @JsonProperty("total_guilds")
    private Long totalGuilds;
    @JsonProperty("external_registrations")
    private Long externalRegistrations;
    @JsonProperty("character_count")
    private Long characterCount;
}
