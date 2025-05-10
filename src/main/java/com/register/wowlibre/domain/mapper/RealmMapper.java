package com.register.wowlibre.domain.mapper;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

public class RealmMapper {


    public static RealmModel toModel(RealmEntity server) {
        if (server == null) {
            return null;
        }
        return RealmModel.builder()
                .id(server.getId())
                .avatar(server.getAvatarUrl())
                .name(server.getName())
                .creationDate(server.getCreatedAt())
                .ip(server.getHost())
                .emulator(server.getEmulator())
                .status(server.isStatus())
                .apiSecret(server.getApiSecret())
                .expansion(server.getExpansionId())
                .webSite(server.getWeb())
                .avatar(server.getAvatarUrl())
                .password(server.getPassword())
                .apiKey(server.getApiKey())
                .jwt(server.getJwt())
                .expirationDate(server.getExpirationDate())
                .refreshToken(server.getRefreshToken())
                .realmlist(server.getRealmlist())
                .build();
    }

    public static RealmEntity toEntity(RealmModel server) {
        if (server == null) {
            return null;
        }

        RealmEntity realmEntity = new RealmEntity();
        realmEntity.setId(server.id);
        realmEntity.setName(server.name);
        realmEntity.setEmulator(server.emulator);
        realmEntity.setExpansionId(server.expansion);
        realmEntity.setAvatarUrl(server.avatar);
        realmEntity.setHost(server.ip);
        realmEntity.setApiKey(server.apiKey);
        realmEntity.setStatus(server.status);
        realmEntity.setPassword(server.password);
        realmEntity.setWeb(server.webSite);
        realmEntity.setCreatedAt(server.creationDate);
        realmEntity.setApiSecret(server.apiSecret);
        realmEntity.setRefreshToken(server.refreshToken);
        realmEntity.setJwt(server.jwt);
        realmEntity.setType(server.type);
        realmEntity.setExpirationDate(server.expirationDate);
        realmEntity.setRealmlist(server.realmlist);
        realmEntity.setExternalPassword(server.externalPassword);
        realmEntity.setExternalUsername(server.externalUsername);
        realmEntity.setSalt(server.salt);
        realmEntity.setRetry(server.retry);
        return realmEntity;
    }


}
