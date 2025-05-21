package com.register.wowlibre.infrastructure.repositories.teleport;

import com.register.wowlibre.domain.port.out.teleport.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaTeleportAdapter implements ObtainTeleport, SaveTeleport {
    private final TeleportRepository teleportRepository;

    public JpaTeleportAdapter(TeleportRepository teleportRepository) {
        this.teleportRepository = teleportRepository;
    }

    @Override
    public List<TeleportEntity> findAllTeleport(Long realmId) {
        return teleportRepository.findByRealmId_id(realmId);
    }

    @Override
    public Optional<TeleportEntity> findById(Long id) {
        return teleportRepository.findById(id);
    }

    @Override
    public void saveTeleport(TeleportEntity teleportEntity) {
        teleportRepository.save(teleportEntity);
    }
}
