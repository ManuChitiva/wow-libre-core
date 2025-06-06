package com.register.wowlibre.infrastructure.repositories.server_details;

import com.register.wowlibre.domain.port.out.server_details.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaServerDetailsAdapter implements ObtainServerDetails, DeleteServerDetails {

    private final ServerDetailsRepository serverDetailsRepository;

    public JpaServerDetailsAdapter(ServerDetailsRepository serverDetailsRepository) {
        this.serverDetailsRepository = serverDetailsRepository;
    }

    @Override
    public List<RealmDetailsEntity> findByServerId(RealmEntity server, String transactionId) {
        return serverDetailsRepository.findByRealmId(server);
    }

    @Override
    public void delete(RealmDetailsEntity detailsEntity, String transactionId) {
        serverDetailsRepository.delete(detailsEntity);
    }
}
