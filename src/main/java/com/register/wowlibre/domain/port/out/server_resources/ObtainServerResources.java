package com.register.wowlibre.domain.port.out.server_resources;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServerResources {
    List<RealmResourcesEntity> findByServerId(RealmEntity server, String transactionId);
}
