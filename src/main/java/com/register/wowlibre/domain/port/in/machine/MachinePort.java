package com.register.wowlibre.domain.port.in.machine;

import com.register.wowlibre.domain.dto.*;

import java.util.*;

public interface MachinePort {
    MachineDto evaluate(Long userId, Long accountId, Long characterId, Long realmId, Locale locale,
                        String transactionId);

    MachineDetailDto points(Long userId, Long accountId, Long realmId, String transactionId);
}
