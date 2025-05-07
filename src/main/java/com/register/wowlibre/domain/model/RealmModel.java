package com.register.wowlibre.domain.model;

import lombok.*;

import java.time.*;
import java.util.*;

@Builder
public class RealmModel {
    public Long id;
    public String name;
    public boolean status;
    public String emulator;
    public String avatar;
    public String apiSecret;
    public Integer expansion;
    public String ip;
    public String type;
    public LocalDateTime creationDate;
    public String jwt;
    public String refreshToken;
    public Date expirationDate;
    public String webSite;
    public String password;
    public String apiKey;
    public String realmlist;
    public byte[] salt;
    public String externalPassword;
    public String externalUsername;
    public Long userId;
    public Integer retry;
}
