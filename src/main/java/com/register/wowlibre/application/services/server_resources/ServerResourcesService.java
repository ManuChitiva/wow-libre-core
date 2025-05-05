package com.register.wowlibre.application.services.server_resources;

import com.register.wowlibre.domain.port.in.server_resources.*;
import com.register.wowlibre.domain.port.out.server_resources.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ServerResourcesService implements ServerResourcesPort {
    private final ObtainServerResources obtainServerResources;

    public ServerResourcesService(ObtainServerResources obtainServerResources) {
        this.obtainServerResources = obtainServerResources;
    }

    @Override
    public List<RealmResourcesEntity> findByServerId(RealmEntity server, String transactionId) {
        return obtainServerResources.findByServerId(server, transactionId);
    }
}
