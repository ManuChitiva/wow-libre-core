package com.register.wowlibre.domain.port.out.realm;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainRealmPort {
    List<RealmEntity> findByStatusIsTrue(String transactionId);

    List<RealmEntity> findAll(String transactionId);

    Optional<RealmEntity> findByNameAndExpansionAndStatusIsTrue(String name, String expansion, String transactionId);

    Optional<RealmEntity> findByApiKey(String apikey, String transactionId);

    Optional<RealmEntity> findById(Long id, String transactionId);

    Optional<RealmEntity> findByNameAndExpansion(String name, String expansion, String transactionId);

    List<RealmEntity> findByStatusIsFalse(String transactionId);

    Optional<RealmEntity> findAndIdByUser(Long id, Long userId, String transactionId);

    List<RealmEntity> findByStatusIsFalseAndRetry(Long retry, String transactionId);

}
