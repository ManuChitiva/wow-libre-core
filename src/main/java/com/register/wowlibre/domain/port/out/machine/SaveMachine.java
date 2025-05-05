package com.register.wowlibre.domain.port.out.machine;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveMachine {
    void save(MachineEntity machine, String transactionId);
}
