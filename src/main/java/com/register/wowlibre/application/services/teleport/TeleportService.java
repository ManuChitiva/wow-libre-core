package com.register.wowlibre.application.services.teleport;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.dto.teleport.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
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
    private final RealmPort realmPort;

    public TeleportService(ObtainTeleport obtainTeleport, SaveTeleport saveTeleport, IntegratorPort integratorPort,
                           AccountGamePort accountGamePort, RealmPort realmPort) {
        this.obtainTeleport = obtainTeleport;
        this.saveTeleport = saveTeleport;
        this.integratorPort = integratorPort;
        this.accountGamePort = accountGamePort;
        this.realmPort = realmPort;
    }

    @Override
    public List<TeleportModel> findByAll(Long realmId, Long raceId, String transactionId) {
        Faction faction = WowFactionRace.getById(raceId).getFaction();
        return obtainTeleport.findAllTeleport(realmId).stream()
                .map(this::mapModel)
                .filter(data -> faction == Faction.ALL || data.faction == Faction.ALL || data.faction.equals(faction))
                .toList();
    }

    @Override
    public void save(TeleportDto teleportModel, String transactionId) {

        Optional<RealmEntity> realm = realmPort.findById(teleportModel.getRealmId(), transactionId);

        if (realm.isEmpty()) {
            throw new InternalException("Server Invalid Or Not Found", transactionId);
        }

        obtainTeleport.findByNameAndRealmId(teleportModel.getName(), teleportModel.getRealmId())
                .ifPresent(teleport -> {
                    throw new InternalException("Teleport Already Exists", transactionId);
                });

        Faction faction = Faction.fromString(teleportModel.getFaction());

        TeleportEntity teleport = new TeleportEntity();
        teleport.setImgUrl(teleportModel.getImgUrl());
        teleport.setName(teleportModel.getName());
        teleport.setPositionX(teleportModel.getPositionX());
        teleport.setPositionY(teleportModel.getPositionY());
        teleport.setPositionZ(teleportModel.getPositionZ());
        teleport.setMap(teleportModel.getMap());
        teleport.setOrientation(teleportModel.getOrientation());
        teleport.setZone(teleportModel.getZone());
        teleport.setFaction(faction);
        teleport.setArea(teleportModel.getArea());
        teleport.setRealmId(realm.get());
        saveTeleport.save(teleport);
    }

    @Override
    public void teleport(Long teleportId, Long userId, Long accountId, Long characterId, Long realmId,
                         String transactionId) {
        AccountVerificationDto account = accountGamePort.verifyAccount(userId, accountId, realmId, transactionId);

        obtainTeleport.findById(teleportId).ifPresent(teleport ->
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
                        .zone(teleport.getZone()).build(), transactionId));
    }

    @Override
    public void delete(Long id, Long realmId, String transactionId) {
        Optional<TeleportEntity> teleport = obtainTeleport.findByIdAndRealmId(id, realmId);

        if (teleport.isEmpty()) {
            throw new InternalException("Teleport Not Found", transactionId);
        }

        saveTeleport.delete(teleport.get());
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
                .realmId(teleport.getRealmId().getId())
                .area(teleport.getArea()).build();
    }
}
