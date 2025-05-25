package com.register.wowlibre.domain.port.in.teleport;

import com.register.wowlibre.domain.dto.teleport.*;
import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface TeleportPort {
    List<TeleportModel> findByAll(Long realmId, Long raceId, String transactionId);

    void save(TeleportDto teleportModel, String transactionId);

    void teleport(Long teleportId, Long userId, Long accountId, Long characterId, Long realmId,
                  String transactionId);

    void delete(Long id,Long realmId, String transactionId);

}
