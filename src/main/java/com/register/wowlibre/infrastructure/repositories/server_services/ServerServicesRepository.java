package com.register.wowlibre.infrastructure.repositories.server_services;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerServicesRepository extends CrudRepository<RealmServicesEntity, Long> {
    List<RealmServicesEntity> findByServerId_Id(Long serverId);

    Optional<RealmServicesEntity> findByNameAndServerId_id(String name, Long serverId);

    @Query("SELECT ss FROM RealmServicesEntity ss " +
            "INNER JOIN ss.serverId s " +
            "WHERE ss.amount > 0 AND s.status = true")
    List<RealmServicesEntity> findActiveServerServicesWithAmountGreaterThanZero();

    @Override
    Optional<RealmServicesEntity> findById(Long id);
}
