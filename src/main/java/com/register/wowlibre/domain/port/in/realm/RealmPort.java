package com.register.wowlibre.domain.port.in.realm;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface RealmPort {

    List<RealmDto> findAll(String transactionId);

    void create(RealmCreateDto realmCreateDto, Long userId, String transactionId);

    List<RealmDto> findByStatusIsTrue(String transactionId);

    RealmModel findByNameAndVersionAndStatusIsTrue(String name, Integer expansionId, String transactionId);

    RealmModel findByApiKey(String apiKey, String transactionId);

    Optional<RealmEntity> findById(Long id, String transactionId);


    List<RealmEntity> findByStatusIsTrueServers(String transactionId);

    Optional<RealmEntity> findByIdAndUserId(Long id, Long userId, String transactionId);

    ServerVdpDto findByServerNameAndExpansion(String name, Integer expansionId, Locale locale, String transactionId);
}
