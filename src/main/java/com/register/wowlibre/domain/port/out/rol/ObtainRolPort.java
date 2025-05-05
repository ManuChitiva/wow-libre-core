package com.register.wowlibre.domain.port.out.rol;

import com.register.wowlibre.infrastructure.entities.RolEntity;

import java.util.Optional;

public interface ObtainRolPort {
    Optional<RolEntity> findByName(String name);
}
