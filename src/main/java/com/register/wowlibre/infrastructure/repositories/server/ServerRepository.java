package com.register.wowlibre.infrastructure.repositories.server;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface ServerRepository extends CrudRepository<RealmEntity, Long> {
    List<RealmEntity> findByStatusIsTrue();

    List<RealmEntity> findByStatusIsFalse();

    Optional<RealmEntity> findByNameAndExpansionAndStatusIsTrue(String name, String expansion);

    Optional<RealmEntity> findByNameAndExpansion(String name, String expansion);

    Optional<RealmEntity> findByApiKey(String apiKey);

    List<RealmEntity> findByUserId(Long userId);

    Optional<RealmEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT s FROM RealmEntity s WHERE s.status = false AND (s.retry <= :retry OR s.retry IS NULL)")
    List<RealmEntity> findByStatusIsFalseAndRetry(@Param("retry") Long retry);

}
