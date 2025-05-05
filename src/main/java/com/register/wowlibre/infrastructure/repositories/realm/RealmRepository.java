package com.register.wowlibre.infrastructure.repositories.realm;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface RealmRepository extends CrudRepository<RealmEntity, Long> {
    List<RealmEntity> findByStatusIsTrue();

    @Query("SELECT s FROM RealmEntity s WHERE s.status = false AND (s.retry <= :retry OR s.retry IS NULL)")
    List<RealmEntity> findByStatusIsFalseAndRetry(@Param("retry") Long retry);
}
