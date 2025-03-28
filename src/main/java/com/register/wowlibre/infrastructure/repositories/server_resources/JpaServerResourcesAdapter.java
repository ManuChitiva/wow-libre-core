package com.register.wowlibre.infrastructure.repositories.server_resources;

import com.register.wowlibre.domain.port.out.server_resources.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaServerResourcesAdapter implements ObtainServerResources {

    private final ServerResourcesRepository serverResourcesRepository;

    public JpaServerResourcesAdapter(ServerResourcesRepository serverResourcesRepository) {
        this.serverResourcesRepository = serverResourcesRepository;
    }

    public List<ServerResourcesEntity> findByServerId(ServerEntity server, String transactionId) {
        return serverResourcesRepository.findByServerId(server);
    }

}
