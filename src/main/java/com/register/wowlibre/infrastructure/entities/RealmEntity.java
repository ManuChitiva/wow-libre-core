package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.time.*;
import java.util.*;

@Data
@Entity
@Table(name = "realm")
public class RealmEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String emulator;
    @Column(name = "expansion_id")
    private int expansionId;
    private String type;
    private String host;
    private int port;
    @Column(name = "api_key")
    private String apiKey;
    @Column(name = "api_secret")
    private String apiSecret;
    private String password;
    private String jwt;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "web")
    private String web;
    private String realmlist;
    private boolean status;
    @Column(name = "external_username")
    private String externalUsername;
    @Column(name = "external_password")
    private String externalPassword;
    private byte[] salt;
    private Integer retry;
    private String disclaimer;
    private Long userId;
}
