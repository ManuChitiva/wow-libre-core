package com.register.wowlibre.infrastructure.repositories.machine;

import com.register.wowlibre.domain.port.out.machine.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaMachineAdapter implements ObtainMachine, SaveMachine {
    private final MachineRepository machineRepository;

    public JpaMachineAdapter(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }


    @Override
    public Optional<MachineEntity> findByUserIdAndRealmId(Long userId, Long realmId) {
        return machineRepository.findByUserIdAndRealmId_Id(userId, realmId);
    }

    @Override
    public void save(MachineEntity machine, String transactionId) {
        machineRepository.save(machine);
    }
}
