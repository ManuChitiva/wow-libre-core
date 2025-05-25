package com.register.wowlibre.domain.port.out.teleport;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveTeleport {
    void save(TeleportEntity teleportEntity);
    void delete(TeleportEntity teleportEntity);

}
