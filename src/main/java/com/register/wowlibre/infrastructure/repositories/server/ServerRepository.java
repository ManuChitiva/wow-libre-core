package com.register.wowlibre.infrastructure.repositories.server;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerRepository extends CrudRepository<ServerEntity, Long> {
    List<ServerEntity> findByStatusIsTrue();

    List<ServerEntity> findByStatusIsFalse();

    Optional<ServerEntity> findByNameAndExpansionAndStatusIsTrue(String name, String expansion);

    Optional<ServerEntity> findByNameAndExpansion(String name, String expansion);

    Optional<ServerEntity> findByApiKey(String apiKey);

    List<ServerEntity> findByUserId(Long userId);

}
