package com.register.wowlibre.domain.port.in.realm_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface RealmServicesPort {
    List<RealmServicesModel> findByRealmId(Long serverId, String transactionId);

    RealmServicesModel findByNameAndServerId(RealmServices name, Long serverId, String transactionId);

    List<RealmServicesModel> findByServersAvailableLoa(String transactionId);

    void updateAmount(Long id, Double amount, String transactionId);

    void updateOrCreateAmountByServerId(RealmServices name, RealmEntity server, Double amount, String transactionId);

}
