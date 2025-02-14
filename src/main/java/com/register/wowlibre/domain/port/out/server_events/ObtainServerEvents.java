package com.register.wowlibre.domain.port.out.server_events;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServerEvents {
    List<ServerEventsEntity> findByServerId(ServerEntity server, String transactionId);
}
