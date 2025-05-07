package com.register.wowlibre.domain.port.in.realm;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface RealmPort {

    List<RealmDto> findAll(String transactionId);

    void create(RealmCreateDto realmCreateDto, Long userId, String transactionId);

    List<RealmDto> findByStatusIsTrue(String transactionId);

    ServerModel findByNameAndVersionAndStatusIsTrue(String name, String version, String transactionId);

    ServerModel findByApiKey(String apiKey, String transactionId);

    Optional<RealmEntity> findById(Long id, String transactionId);


    List<RealmEntity> findByStatusIsTrueServers(String transactionId);

    Optional<RealmEntity> findByIdAndUserId(Long id, Long userId, String transactionId);

    ServerVdpDto findByServerNameAndExpansion(String name, String expansion, Locale locale, String transactionId);
}
