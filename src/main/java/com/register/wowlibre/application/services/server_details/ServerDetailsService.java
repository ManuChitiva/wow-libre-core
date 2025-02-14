package com.register.wowlibre.application.services.server_details;

import com.register.wowlibre.domain.port.in.server_details.*;
import com.register.wowlibre.domain.port.out.server_details.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ServerDetailsService implements DeleteServerDetailsPort, ObtainServerDetailsPort {

    private final ObtainServerDetails obtainServerDetails;
    private final DeleteServerDetails deleteServerDetails;

    public ServerDetailsService(ObtainServerDetails obtainServerDetails, DeleteServerDetails deleteServerDetails) {
        this.obtainServerDetails = obtainServerDetails;
        this.deleteServerDetails = deleteServerDetails;
    }

    @Override
    public void delete(ServerDetailsEntity detailsEntity, String transactionId) {
        deleteServerDetails.delete(detailsEntity, transactionId);
    }

    @Override
    public List<ServerDetailsEntity> findByServerId(ServerEntity server, String transactionId) {
        return obtainServerDetails.findByServerId(server, transactionId);
    }
}
