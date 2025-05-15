package com.register.wowlibre.application.services.teleport;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.dto.teleport.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.teleport.*;
import com.register.wowlibre.domain.port.out.teleport.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TeleportService implements TeleportPort {
    private final ObtainTeleport obtainTeleport;
    private final SaveTeleport saveTeleport;
    private final IntegratorPort integratorPort;
    private final AccountGamePort accountGamePort;

    public TeleportService(ObtainTeleport obtainTeleport, SaveTeleport saveTeleport, IntegratorPort integratorPort,
                           AccountGamePort accountGamePort) {
        this.obtainTeleport = obtainTeleport;
        this.saveTeleport = saveTeleport;
        this.integratorPort = integratorPort;
        this.accountGamePort = accountGamePort;
    }

    @Override
    public List<TeleportModel> findByAll(Long raceId, String transactionId) {
        Faction faction = WowFactionRace.getById(raceId).getFaction();

        return obtainTeleport.findAllTeleport().stream()
                .map(this::mapModel)
                .filter(data -> faction == Faction.ALL || data.faction == Faction.ALL || data.faction.equals(faction))
                .toList();
    }

    @Override
    public void save(TeleportDto teleportModel, String transactionId) {
        TeleportEntity teleport = new TeleportEntity();
        teleport.setImgUrl(teleportModel.getImgUrl());
        teleport.setPositionX(teleportModel.getPositionX());
        teleport.setPositionY(teleportModel.getPositionY());
        teleport.setPositionZ(teleportModel.getPositionZ());
        teleport.setMap(teleportModel.getMap());
        teleport.setOrientation(teleportModel.getOrientation());
        teleport.setZone(teleportModel.getZona());
        teleport.setArea(teleportModel.getArea());
        saveTeleport.saveTeleport(teleport);
    }

    @Override
    public void teleport(Long teleportId, Long userId, Long accountId, Long characterId, Long realmId,
                         String transactionId) {
        AccountVerificationDto account = accountGamePort.verifyAccount(userId, accountId, realmId, transactionId);

        obtainTeleport.findById(teleportId).ifPresent(teleport -> {
            integratorPort.teleport(account.realm().getHost(), account.realm().getJwt(),
                    TeleportRequest.builder()
                            .map(teleport.getMap())
                            .positionX(teleport.getPositionX())
                            .positionY(teleport.getPositionY())
                            .positionZ(teleport.getPositionZ())
                            .userId(userId)
                            .orientation(teleport.getOrientation())
                            .characterId(characterId)
                            .accountId(accountId)
                            .zone(teleport.getZone()).build(), transactionId);
        });
    }

    private TeleportModel mapModel(TeleportEntity teleport) {
        return TeleportModel.builder()
                .id(teleport.getId())
                .zone(teleport.getZone())
                .name(teleport.getName())
                .imgUrl(teleport.getImgUrl())
                .positionX(teleport.getPositionX())
                .positionY(teleport.getPositionY())
                .positionZ(teleport.getPositionZ())
                .map(teleport.getMap())
                .orientation(teleport.getOrientation())
                .faction(teleport.getFaction())
                .area(teleport.getArea()).build();
    }
}
