package com.register.wowlibre.domain.port.out.realm_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainRealmServices {
    Optional<RealmServicesEntity> findById(Long id);

    List<RealmServicesEntity> findByRealmId(Long realmId, String transactionId);

    Optional<RealmServicesEntity> findByNameAndRealmId(RealmServices name, Long realmId, String transactionId);

    List<RealmServicesEntity> findByServersAvailableRequestLoa(String transactionId);

}
