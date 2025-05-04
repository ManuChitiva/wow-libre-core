package com.register.wowlibre.domain.port.out.server_details;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServerDetails {
    List<ServerDetailsEntity> findByServerId(RealmEntity server, String transactionId);
}
