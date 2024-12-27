package com.register.wowlibre.domain.mapper;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

public class ServerMapper {


    public static ServerModel toModel(ServerEntity server) {
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

    public static ServerEntity toEntity(ServerModel server) {
        if (server == null) {
            return null;
        }

        ServerEntity serverEntity = new ServerEntity();
        serverEntity.setId(server.id);
        serverEntity.setName(server.name);
        serverEntity.setEmulator(server.emulator);
        serverEntity.setExpansion(server.expansion);
        serverEntity.setAvatar(server.avatar);
        serverEntity.setIp(server.ip);
        serverEntity.setApiKey(server.apiKey);
        serverEntity.setStatus(server.status);
        serverEntity.setPassword(server.password);
        serverEntity.setWebSite(server.webSite);
        serverEntity.setCreationDate(server.creationDate);
        serverEntity.setApiSecret(server.apiSecret);
        serverEntity.setRefreshToken(server.refreshToken);
        serverEntity.setJwt(server.jwt);
        serverEntity.setType(server.type);
        serverEntity.setExpirationDate(server.expirationDate);
        serverEntity.setRealmlist(server.realmlist);
        serverEntity.setExternalPassword(server.externalPassword);
        serverEntity.setExternalUsername(server.externalUsername);
        serverEntity.setSalt(server.salt);
        serverEntity.setUserId(server.userId);
        serverEntity.setRetry(0);
        return serverEntity;
    }


}
