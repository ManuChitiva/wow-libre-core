package com.register.wowlibre.domain.port.out.realm_services;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveRealmServices {
    void save(RealmServicesEntity realmServicesEntity, String transactionId);
}
