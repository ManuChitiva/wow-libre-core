package com.register.wowlibre.infrastructure.repositories.realm_advertising;

import com.register.wowlibre.domain.port.out.realm_advertising.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaRealmAdvertisingAdapter implements SaveRealmAdvertising, ObtainRealmAdvertising {

    private final RealmAdvertisingRepository realmAdvertisingRepository;

    public JpaRealmAdvertisingAdapter(RealmAdvertisingRepository realmAdvertisingRepository) {
        this.realmAdvertisingRepository = realmAdvertisingRepository;
    }

    @Override
    public Optional<RealmAdvertisingEntity> findByRealmId(Long realmId, String language) {
        return realmAdvertisingRepository.findByRealmId_idAndLanguage(realmId, language.toUpperCase());
    }

    @Override
    public List<RealmAdvertisingEntity> findByLanguage(String language, String transactionId) {
        return realmAdvertisingRepository.findByLanguage(language);
    }

    @Override
    public void save(RealmAdvertisingEntity realmAdvertisingEntity) {
        realmAdvertisingRepository.save(realmAdvertisingEntity);
    }

    @Override
    public void delete(RealmAdvertisingEntity realmAdvertisingEntity) {
        realmAdvertisingRepository.delete(realmAdvertisingEntity);
    }
}
