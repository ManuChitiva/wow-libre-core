package com.register.wowlibre.domain.port.out.teleport;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainTeleport {
    List<TeleportEntity> findAllTeleport();

    Optional<TeleportEntity> findById(Long id);
}
