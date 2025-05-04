package com.register.wowlibre.domain.port.out.server_services;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServiceServices {
    List<RealmServicesEntity> findByServerId(Long serverId, String transactionId);

    Optional<RealmServicesEntity> findByNameAndServerId(String name, Long serverId, String transactionId);

    List<RealmServicesEntity> findByServersAvailableRequestLoa(String transactionId);

    Optional<RealmServicesEntity> findById(Long id);
}
