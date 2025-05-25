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
    public Optional<TeleportEntity> findByIdAndRealmId(Long id, Long realmId) {
        return teleportRepository.findByIdAndRealmId_id(id, realmId);
    }

    @Override
    public Optional<TeleportEntity> findByNameAndRealmId(String name, Long realmId) {
        return teleportRepository.findByNameAndRealmId_id(name, realmId);
    }

    @Override
    public void save(TeleportEntity teleportEntity) {
        teleportRepository.save(teleportEntity);

    }

    @Override
    public void delete(TeleportEntity teleportEntity) {
        teleportRepository.delete(teleportEntity);
    }
}
