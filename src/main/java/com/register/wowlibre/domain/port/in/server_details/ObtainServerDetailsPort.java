package com.register.wowlibre.domain.port.in.server_details;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainServerDetailsPort {
    List<RealmDetailsEntity> findByServerId(RealmEntity server, String transactionId);
}
