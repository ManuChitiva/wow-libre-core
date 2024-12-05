package com.register.wowlibre.domain.dto;

import lombok.*;

import java.time.*;

@Data
public class ServerDto {
    private Long id;
    private String name;
    private boolean status;
    private String emulator;
    private String avatar;
    private String expansion;
    private LocalDateTime creationDate;
    private String webSite;
    private String expName;
    private String apiKey;
}
