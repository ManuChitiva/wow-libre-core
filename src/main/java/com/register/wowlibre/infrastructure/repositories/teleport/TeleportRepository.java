package com.register.wowlibre.infrastructure.repositories.teleport;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface TeleportRepository extends CrudRepository<TeleportEntity, Long> {

    List<TeleportEntity> findByRealmId_id(Long realmId);
}
