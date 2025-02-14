package com.register.wowlibre.domain.port.in.server;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ServerPort {

    List<ServerDto> findByUserId(Long userId, String transactionId);

    void create(ServerCreateDto serverCreateDto, Long userId, String transactionId);

    List<ServerDto> findByStatusIsTrue(String transactionId);

    ServerModel findByNameAndVersionAndStatusIsTrue(String name, String version, String transactionId);

    ServerModel findByApiKey(String apiKey, String transactionId);

    Optional<ServerEntity> findById(Long id, String transactionId);


    List<ServerEntity> findByStatusIsTrueServers(String transactionId);

    Optional<ServerEntity> findByIdAndUserId(Long id, Long userId, String transactionId);

    ServerVdpDto findByServerNameAndExpansion(String name, String expansion, String transactionId);
}
