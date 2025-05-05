package com.register.wowlibre.application.services.rol;

import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.rol.*;
import com.register.wowlibre.domain.port.out.rol.*;
import org.springframework.stereotype.*;

@Service
public class RolService implements RolPort {
    private final ObtainRolPort obtainRolPort;

    public RolService(ObtainRolPort obtainRolPort) {
        this.obtainRolPort = obtainRolPort;
    }

    @Override
    public RolModel findByName(String name, String transactionId) {
        return obtainRolPort.findByName(name).map(RolMapper::toModel).orElse(null);
    }
}
