package com.register.wowlibre.domain.port.out.realm;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveRealmPort {
    void save(RealmEntity realmEntity, String transactionId);
}
