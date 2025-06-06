package com.register.wowlibre.infrastructure.repositories.server_resources;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerResourcesRepository extends CrudRepository<RealmResourcesEntity, Long> {
    List<RealmResourcesEntity> findByRealmId(RealmEntity serverId);
}
