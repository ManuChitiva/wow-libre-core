package com.register.wowlibre.application.services.server_events;

import com.register.wowlibre.domain.port.in.server_events.*;
import com.register.wowlibre.domain.port.out.server_events.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ServerEventsService implements ServerEventsPort {

    private final ObtainServerEvents obtainServerEvents;

    public ServerEventsService(ObtainServerEvents obtainServerEvents) {
        this.obtainServerEvents = obtainServerEvents;
    }

    @Override
    public List<ServerEventsEntity> findByServerId(ServerEntity server, String transactionId) {
        return obtainServerEvents.findByServerId(server, transactionId);
    }
}
