package com.register.wowlibre.domain.port.in.server_resources;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ServerResourcesPort {
    List<ServerResourcesEntity> findByServerId(RealmEntity server, String transactionId);
}
