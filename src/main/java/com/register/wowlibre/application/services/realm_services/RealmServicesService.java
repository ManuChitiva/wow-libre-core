package com.register.wowlibre.application.services.realm_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.realm_services.*;
import com.register.wowlibre.domain.port.out.realm_services.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RealmServicesService implements RealmServicesPort {
    private final ObtainRealmServices obtainRealmServices;
    private final SaveRealmServices saveRealmServices;

    public RealmServicesService(ObtainRealmServices obtainRealmServices, SaveRealmServices saveRealmServices) {
        this.obtainRealmServices = obtainRealmServices;
        this.saveRealmServices = saveRealmServices;
    }

    @Override
    public List<RealmServicesModel> findByRealmId(Long serverId, String transactionId) {
        return obtainRealmServices.findByRealmId(serverId, transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public RealmServicesModel findByNameAndServerId(RealmServices name, Long serverId, String transactionId) {
        return obtainRealmServices.findByNameAndRealmId(name, serverId, transactionId).map(this::mapToModel).orElse(null);
    }

    @Override
    public List<RealmServicesModel> findByServersAvailableLoa(String transactionId) {
        return obtainRealmServices.findByServersAvailableRequestLoa(transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public void updateAmount(Long id, Double amount, String transactionId) {
        Optional<RealmServicesEntity> updateAmount = obtainRealmServices.findById(id);

        if (updateAmount.isEmpty()) {
            throw new InternalException("The available money from the loan limit could not be updated", transactionId);
        }
        RealmServicesEntity update = updateAmount.get();
        update.setAmount(amount);
        saveRealmServices.save(update, transactionId);

    }

    @Override
    public void updateOrCreateAmountByServerId(RealmServices name, RealmEntity realm, Double amount,
                                               String transactionId) {
        Optional<RealmServicesEntity> existingService =
                obtainRealmServices.findByNameAndRealmId(name, realm.getId(), transactionId);


        RealmServicesEntity serviceEntity = existingService.orElseGet(() -> {
            RealmServicesEntity newService = new RealmServicesEntity();
            newService.setRealmId(realm);
            newService.setName(RealmServices.BANK);
            return newService;
        });

        serviceEntity.setAmount(amount);

        saveRealmServices.save(serviceEntity, transactionId);
    }

    private RealmServicesModel mapToModel(RealmServicesEntity realmServicesEntity) {
        return new RealmServicesModel(realmServicesEntity.getId(), null,
                realmServicesEntity.getAmount(), realmServicesEntity.getRealmId().getId(),
                realmServicesEntity.getRealmId().getName());
    }
}
