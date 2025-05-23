package com.register.wowlibre.infrastructure.repositories.realm;

import com.register.wowlibre.domain.port.out.realm.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaRealmAdapter implements ObtainRealmPort, SaveRealmPort {
    private final RealmRepository realmRepository;

    public JpaRealmAdapter(RealmRepository realmRepository) {
        this.realmRepository = realmRepository;
    }

    @Override
    public List<RealmEntity> findAll(String transactionId) {
        return realmRepository.findAll();
    }

    @Override
    public List<RealmEntity> findByStatusIsTrue(String transactionId) {
        return realmRepository.findByStatusIsTrue();
    }

    @Override
    public Optional<RealmEntity> findByNameAndExpansionAndStatusIsTrue(String name, Integer expansionId) {
        return realmRepository.findByNameAndExpansionIdAndStatusIsTrue(name, expansionId);
    }

    @Override
    public Optional<RealmEntity> findByApiKey(String apikey, String transactionId) {
        return realmRepository.findByApiKey(apikey);
    }

    @Override
    public Optional<RealmEntity> findById(Long id, String transactionId) {
        return realmRepository.findById(id);
    }

    @Override
    public Optional<RealmEntity> findByNameAndExpansion(String name, Integer expansionId, String transactionId) {
        return realmRepository.findByNameAndExpansionId(name, expansionId);
    }

    @Override
    public List<RealmEntity> findByStatusIsFalse(String transactionId) {
        return null;
    }

    @Override
    public Optional<RealmEntity> findAndIdByUser(Long id, Long userId, String transactionId) {
        return null;
    }

    @Override
    public List<RealmEntity> findByStatusIsFalseAndRetry(Long retry, String transactionId) {
        return realmRepository.findByStatusIsFalseAndRetry(retry);
    }

    @Override
    public void save(RealmEntity realmEntity, String transactionId) {
        realmRepository.save(realmEntity);
    }
}

