package com.register.wowlibre.infrastructure.repositories.server_events;

import com.register.wowlibre.domain.port.out.server_events.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaServerEventsAdapter implements ObtainServerEvents {

    private final ServerEventsRepository serverEventsRepository;

    public JpaServerEventsAdapter(ServerEventsRepository serverEventsRepository) {
        this.serverEventsRepository = serverEventsRepository;
    }

    @Override
    public List<RealmEventsEntity> findByServerId(RealmEntity server, String transactionId) {
        return serverEventsRepository.findByRealmId(server);
    }
}
