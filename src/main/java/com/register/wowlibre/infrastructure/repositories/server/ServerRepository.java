package com.register.wowlibre.infrastructure.repositories.server;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface ServerRepository extends CrudRepository<ServerEntity, Long> {
    List<ServerEntity> findByStatusIsTrue();

    List<ServerEntity> findByStatusIsFalse();

    Optional<ServerEntity> findByNameAndExpansionAndStatusIsTrue(String name, String expansion);

    Optional<ServerEntity> findByNameAndExpansion(String name, String expansion);

    Optional<ServerEntity> findByApiKey(String apiKey);

    List<ServerEntity> findByUserId(Long userId);

    Optional<ServerEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT s FROM ServerEntity s WHERE s.status = false AND (s.retry <= :retry OR s.retry IS NULL)")
    List<ServerEntity> findByStatusIsFalseAndRetry(@Param("retry") Long retry);

}
