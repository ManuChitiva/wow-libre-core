package com.register.wowlibre.domain.port.in.server_services;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ServerServicesPort {
    List<ServerServicesModel> findByServerId(Long serverId, String transactionId);

    ServerServicesModel findByNameAndServerId(String name, Long serverId, String transactionId);

    List<ServerServicesModel> findByServersAvailableLoa(String transactionId);

    void updateAmount(Long id, Double amount, String transactionId);

    void updateOrCreateAmountByServerId(String name, ServerEntity server, Double amount, String transactionId);

}
