package com.register.wowlibre.domain.port.out.machine;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainMachine {
    Optional<MachineEntity> findByUserIdAndServerId(Long userId, Long serverId);
}
