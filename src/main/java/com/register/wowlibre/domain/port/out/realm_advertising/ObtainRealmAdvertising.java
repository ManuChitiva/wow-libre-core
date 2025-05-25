package com.register.wowlibre.domain.port.out.realm_advertising;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainRealmAdvertising {
    Optional<RealmAdvertisingEntity> findByRealmId(Long realmId, String language);

    List<RealmAdvertisingEntity> findByLanguage(String language, String transactionId);

}
