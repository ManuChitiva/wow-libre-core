package com.register.wowlibre.domain.model;

public record AccountGameModel(Long id, String username, Long accountId,
                               String email, String realm, Long serverId,
                               String expansion,
                               String avatar, String webSite, boolean status, String realmlist) {
}
