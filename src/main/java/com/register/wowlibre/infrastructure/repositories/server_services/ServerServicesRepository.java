package com.register.wowlibre.infrastructure.repositories.server_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface ServerServicesRepository extends CrudRepository<RealmServicesEntity, Long> {
    List<RealmServicesEntity> findByRealmId_Id(Long realmId);

    Optional<RealmServicesEntity> findByNameAndRealmId_Id(RealmServices name, Long realmId_id);

    @Query("SELECT ss FROM RealmServicesEntity ss " +
            "INNER JOIN ss.realmId s " +
            "WHERE ss.amount > 0 AND s.status = true AND ss.name = :serviceName")
    List<RealmServicesEntity> findActiveRealmServicesWithAmountGreaterThanZero(@Param("serviceName") RealmServices serviceName);

}
