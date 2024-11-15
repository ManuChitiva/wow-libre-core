package com.register.wowlibre.domain.model;

import lombok.*;

import java.time.*;
import java.util.*;

@Builder
public class ServerModel {
    public Long id;
    public String name;
    public boolean status;
    public String emulator;
    public String avatar;
    public String apiSecret;
    public String expansion;
    public String ip;
    public LocalDateTime creationDate;
    public String jwt;
    public String refreshToken;
    public Date expirationDate;
    public String webSite;
    public String password;
    public String apiKey;
    public String realmlist;
}
