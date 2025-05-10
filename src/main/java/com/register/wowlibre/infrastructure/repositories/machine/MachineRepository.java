package com.register.wowlibre.infrastructure.repositories.machine;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface MachineRepository extends CrudRepository<MachineEntity, Long> {
    Optional<MachineEntity> findByUserIdAndRealmId_Id(Long userId, Long realmId);
}
