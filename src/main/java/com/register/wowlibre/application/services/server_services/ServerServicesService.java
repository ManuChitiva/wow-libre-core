package com.register.wowlibre.application.services.server_services;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.server_services.*;
import com.register.wowlibre.domain.port.out.server_services.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ServerServicesService implements ServerServicesPort {
    private final ObtainServiceServices obtainServiceServices;
    private final SaveServiceServices saveServiceServices;

    public ServerServicesService(ObtainServiceServices obtainServiceServices, SaveServiceServices saveServiceServices) {
        this.obtainServiceServices = obtainServiceServices;
        this.saveServiceServices = saveServiceServices;
    }

    @Override
    public List<ServerServicesModel> findByServerId(Long serverId, String transactionId) {
        return obtainServiceServices.findByServerId(serverId, transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public ServerServicesModel findByNameAndServerId(String name, Long serverId, String transactionId) {
        return obtainServiceServices.findByNameAndServerId(name, serverId, transactionId).map(this::mapToModel).orElse(null);
    }

    @Override
    public List<ServerServicesModel> findByServersAvailableLoa(String transactionId) {
        return obtainServiceServices.findByServersAvailableRequestLoa(transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public void updateAmount(Long id, Double amount, String transactionId) {
        Optional<RealmServicesEntity> updateAmount = obtainServiceServices.findById(id);

        if (updateAmount.isEmpty()) {
            throw new InternalException("The available money from the loan limit could not be updated", transactionId);
        }
        RealmServicesEntity update = updateAmount.get();
        update.setAmount(amount);
        saveServiceServices.save(update, transactionId);

    }

    @Override
    public void updateOrCreateAmountByServerId(String name, RealmEntity server, Double amount, String transactionId) {
        Optional<RealmServicesEntity> existingService =
                obtainServiceServices.findByNameAndServerId(name, server.getId(), transactionId);


        RealmServicesEntity serviceEntity = existingService.orElseGet(() -> {
            RealmServicesEntity newService = new RealmServicesEntity();
            newService.setServerId(server);
            newService.setName(name);
            return newService;
        });

        serviceEntity.setAmount(amount);

        saveServiceServices.save(serviceEntity, transactionId);
    }

    private ServerServicesModel mapToModel(RealmServicesEntity realmServicesEntity) {
        return new ServerServicesModel(realmServicesEntity.getId(), realmServicesEntity.getName(),
                realmServicesEntity.getAmount(), realmServicesEntity.getServerId().getId(),
                realmServicesEntity.getServerId().getName());
    }
}
