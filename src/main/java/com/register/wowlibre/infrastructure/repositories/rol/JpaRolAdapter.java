package com.register.wowlibre.infrastructure.repositories.rol;

import com.register.wowlibre.domain.port.out.rol.ObtainRolPort;
import com.register.wowlibre.infrastructure.entities.RolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaRolAdapter implements ObtainRolPort {
    private final RolRepository rolRepository;

    @Autowired
    public JpaRolAdapter(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Optional<RolEntity> findByName(String name) {
        return rolRepository.findByNameAndStatusIsTrue(name);
    }
}
