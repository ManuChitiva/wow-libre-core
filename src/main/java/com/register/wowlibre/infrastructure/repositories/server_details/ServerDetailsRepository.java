package com.register.wowlibre.infrastructure.repositories.server_details;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerDetailsRepository extends CrudRepository<RealmDetailsEntity, Long> {

    List<RealmDetailsEntity> findByRealmId(RealmEntity serverId);
}
