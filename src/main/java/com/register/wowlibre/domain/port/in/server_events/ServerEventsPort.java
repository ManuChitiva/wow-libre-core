package com.register.wowlibre.domain.port.in.server_events;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ServerEventsPort {
    List<RealmEventsEntity> findByServerId(RealmEntity server, String transactionId);
}
