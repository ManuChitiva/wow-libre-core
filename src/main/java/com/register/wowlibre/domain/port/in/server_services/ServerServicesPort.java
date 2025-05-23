package com.register.wowlibre.domain.port.in.server_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ServerServicesPort {
    List<ServerServicesModel> findByServerId(Long serverId, String transactionId);

    ServerServicesModel findByNameAndServerId(RealmServices name, Long serverId, String transactionId);

    List<ServerServicesModel> findByServersAvailableLoa(String transactionId);

    void updateAmount(Long id, Double amount, String transactionId);

    void updateOrCreateAmountByServerId(RealmServices name, RealmEntity server, Double amount, String transactionId);

}
