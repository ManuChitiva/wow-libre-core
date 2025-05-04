package com.register.wowlibre.domain.port.out.server_services;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveServiceServices {
    void save(RealmServicesEntity realmServicesEntity, String transactionId);
}
