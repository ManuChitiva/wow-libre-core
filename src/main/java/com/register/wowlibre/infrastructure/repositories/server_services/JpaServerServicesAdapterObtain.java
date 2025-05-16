package com.register.wowlibre.infrastructure.repositories.server_services;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.port.out.realm_services.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaServerServicesAdapterObtain implements ObtainRealmServices, SaveRealmServices {
    private final ServerServicesRepository serverServicesRepository;

    public JpaServerServicesAdapterObtain(ServerServicesRepository serverServicesRepository) {
        this.serverServicesRepository = serverServicesRepository;
    }

    @Override
    public List<RealmServicesEntity> findByRealmId(Long realmId, String transactionId) {
        return serverServicesRepository.findByRealmId_Id(realmId);
    }

    @Override
    public Optional<RealmServicesEntity> findByNameAndRealmId(RealmServices name, Long realmId, String transactionId) {
        return serverServicesRepository.findByNameAndRealmId_Id(name, realmId);
    }

    @Override
    public List<RealmServicesEntity> findByServersAvailableRequestLoa(String transactionId) {
        return serverServicesRepository.findActiveRealmServicesWithAmountGreaterThanZero(RealmServices.BANK);
    }

    @Override
    public Optional<RealmServicesEntity> findById(Long id) {
        return serverServicesRepository.findById(id);
    }

    @Override
    public void save(RealmServicesEntity realmServicesEntity, String transactionId) {
        serverServicesRepository.save(realmServicesEntity);
    }
}
