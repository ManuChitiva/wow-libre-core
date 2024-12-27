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
    public List<ServerEntity> findByUser(Long userId, String transactionId) {
        return serverRepository.findByUserId(userId);
    }

    @Override
    public List<ServerEntity> findByStatusIsTrue(String transactionId) {
        return serverRepository.findByStatusIsTrue();
    }

    @Override
    public Optional<ServerEntity> findByNameAndExpansionAndStatusIsTrue(String name, String version,
                                                                        String transactionId) {
        return serverRepository.findByNameAndExpansionAndStatusIsTrue(name, version);
    }

    @Override
    public Optional<ServerEntity> findByApiKey(String apikey, String transactionId) {
        return serverRepository.findByApiKey(apikey);
    }

    @Override
    public Optional<ServerEntity> findById(Long id, String transactionId) {
        return serverRepository.findById(id);
    }

    @Override
    public Optional<ServerEntity> findByNameAndExpansion(String name, String expansion, String transactionId) {
        return serverRepository.findByNameAndExpansionAndStatusIsTrue(name, expansion);
    }

    @Override
    public List<ServerEntity> findByStatusIsFalse(String transactionId) {
        return serverRepository.findByStatusIsFalse();
    }

    @Override
    public Optional<ServerEntity> findAndIdByUser(Long id, Long userId, String transactionId) {
        return serverRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<ServerEntity> findByStatusIsFalseAndRetry(Long retry, String transactionId) {
        return serverRepository.findByStatusIsFalseAndRetry(retry);
    }

    @Override
    public void save(ServerEntity serverEntity, String transactionId) {
        serverRepository.save(serverEntity);
    }
}

