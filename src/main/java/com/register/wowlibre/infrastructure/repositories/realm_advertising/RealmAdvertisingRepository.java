package com.register.wowlibre.infrastructure.repositories.realm_advertising;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface RealmAdvertisingRepository extends CrudRepository<RealmAdvertisingEntity, Long> {
    Optional<RealmAdvertisingEntity> findByRealmId_idAndLanguage(Long realmId, String language);

    List<RealmAdvertisingEntity> findByLanguage(String language);
}
