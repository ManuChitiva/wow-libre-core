package com.register.wowlibre.domain.port.out.server;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServerPort {
    List<ServerEntity> findByUser(Long userId, String transactionId);

    List<ServerEntity> findByStatusIsTrue(String transactionId);

    Optional<ServerEntity> findByNameAndExpansionAndStatusIsTrue(String name, String expansion, String transactionId);

    Optional<ServerEntity> findByApiKey(String apikey, String transactionId);

    Optional<ServerEntity> findById(Long id, String transactionId);

    Optional<ServerEntity> findByNameAndExpansion(String name, String expansion, String transactionId);

    List<ServerEntity> findByStatusIsFalse(String transactionId);

    Optional<ServerEntity> findAndIdByUser(Long id, Long userId, String transactionId);

    List<ServerEntity> findByStatusIsFalseAndRetry(Long retry, String transactionId);

}
