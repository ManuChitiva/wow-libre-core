package com.register.wowlibre.domain.port.out.realm_advertising;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveRealmAdvertising {
    void save(RealmAdvertisingEntity realmAdvertisingEntity);

    void delete(RealmAdvertisingEntity realmAdvertisingEntity);

}
