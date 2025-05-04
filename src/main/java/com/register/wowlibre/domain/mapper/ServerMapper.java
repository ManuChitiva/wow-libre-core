package com.register.wowlibre.domain.mapper;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

public class ServerMapper {


    public static ServerModel toModel(RealmEntity server) {
        if (server == null) {
            return null;
        }
        return ServerModel.builder()
                .id(server.getId())
                .avatar(server.getAvatar())
                .name(server.getName())
                .creationDate(server.getCreationDate())
                .ip(server.getIp())
                .emulator(server.getEmulator())
                .status(server.isStatus())
                .apiSecret(server.getApiSecret())
                .expansion(server.getExpansion())
                .webSite(server.getWebSite())
                .avatar(server.getAvatar())
                .password(server.getPassword())
                .apiKey(server.getApiKey())
                .jwt(server.getJwt())
                .expirationDate(server.getExpirationDate())
                .refreshToken(server.getRefreshToken())
                .realmlist(server.getRealmlist())
                .build();
    }

    public static RealmEntity toEntity(ServerModel server) {
        if (server == null) {
            return null;
        }

        RealmEntity realmEntity = new RealmEntity();
        realmEntity.setId(server.id);
        realmEntity.setName(server.name);
        realmEntity.setEmulator(server.emulator);
        realmEntity.setExpansion(server.expansion);
        realmEntity.setAvatar(server.avatar);
        realmEntity.setIp(server.ip);
        realmEntity.setApiKey(server.apiKey);
        realmEntity.setStatus(server.status);
        realmEntity.setPassword(server.password);
        realmEntity.setWebSite(server.webSite);
        realmEntity.setCreationDate(server.creationDate);
        realmEntity.setApiSecret(server.apiSecret);
        realmEntity.setRefreshToken(server.refreshToken);
        realmEntity.setJwt(server.jwt);
        realmEntity.setType(server.type);
        realmEntity.setExpirationDate(server.expirationDate);
        realmEntity.setRealmlist(server.realmlist);
        realmEntity.setExternalPassword(server.externalPassword);
        realmEntity.setExternalUsername(server.externalUsername);
        realmEntity.setSalt(server.salt);
        realmEntity.setUserId(server.userId);
        realmEntity.setRetry(server.retry);
        return realmEntity;
    }


}
