package com.register.wowlibre.infrastructure.repositories.server_events;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ServerEventsRepository extends CrudRepository<RealmEventsEntity, Long> {

    List<RealmEventsEntity> findByRealmId(RealmEntity server);

}
