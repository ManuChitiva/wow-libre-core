package com.register.wowlibre.infrastructure.repositories.server;

import com.register.wowlibre.domain.port.out.server.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaServerAdapter implements ObtainServerPort, SaveServerPort {
    private final ServerRepository serverRepository;

    public JpaServerAdapter(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public List<RealmEntity> findByUser(Long userId, String transactionId) {
        return serverRepository.findByUserId(userId);
    }

    @Override
    public List<RealmEntity> findByStatusIsTrue(String transactionId) {
        return serverRepository.findByStatusIsTrue();
    }

    @Override
    public Optional<RealmEntity> findByNameAndExpansionAndStatusIsTrue(String name, String version,
                                                                       String transactionId) {
        return serverRepository.findByNameAndExpansionAndStatusIsTrue(name, version);
    }

    @Override
    public Optional<RealmEntity> findByApiKey(String apikey, String transactionId) {
        return serverRepository.findByApiKey(apikey);
    }

    @Override
    public Optional<RealmEntity> findById(Long id, String transactionId) {
        return serverRepository.findById(id);
    }

    @Override
    public Optional<RealmEntity> findByNameAndExpansion(String name, String expansion, String transactionId) {
        return serverRepository.findByNameAndExpansionAndStatusIsTrue(name, expansion);
    }

    @Override
    public List<RealmEntity> findByStatusIsFalse(String transactionId) {
        return serverRepository.findByStatusIsFalse();
    }

    @Override
    public Optional<RealmEntity> findAndIdByUser(Long id, Long userId, String transactionId) {
        return serverRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<RealmEntity> findByStatusIsFalseAndRetry(Long retry, String transactionId) {
        return serverRepository.findByStatusIsFalseAndRetry(retry);
    }

    @Override
    public void save(RealmEntity realmEntity, String transactionId) {
        serverRepository.save(realmEntity);
    }
}

