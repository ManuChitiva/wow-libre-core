package com.register.wowlibre.application.services.server_services;

import com.register.wowlibre.domain.enums.*;
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
        return obtainServiceServices.findByRealmId(serverId, transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public ServerServicesModel findByNameAndServerId(RealmServices name, Long serverId, String transactionId) {
        return obtainServiceServices.findByNameAndRealmId(name, serverId, transactionId).map(this::mapToModel).orElse(null);
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
    public void updateOrCreateAmountByServerId(RealmServices name, RealmEntity server, Double amount,
                                               String transactionId) {
        Optional<RealmServicesEntity> existingService =
                obtainServiceServices.findByNameAndRealmId(name, server.getId(), transactionId);


        RealmServicesEntity serviceEntity = existingService.orElseGet(() -> {
            RealmServicesEntity newService = new RealmServicesEntity();
            newService.setRealmId(null);
            newService.setName(null);
            return newService;
        });

        serviceEntity.setAmount(amount);

        saveServiceServices.save(serviceEntity, transactionId);
    }

    private ServerServicesModel mapToModel(RealmServicesEntity realmServicesEntity) {
        return new ServerServicesModel(realmServicesEntity.getId(), null,
                realmServicesEntity.getAmount(), realmServicesEntity.getRealmId().getId(),
                realmServicesEntity.getRealmId().getName());
    }
}
