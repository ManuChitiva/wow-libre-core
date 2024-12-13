package com.register.wowlibre.application.services.server;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.server.*;
import com.register.wowlibre.domain.port.out.server.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.time.*;
import java.util.*;

@Repository
public class ServerService implements ServerPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerService.class);

    private static final String AVATAR_SERVER_DEFAULT = "https://upload.wikimedia" +
            ".org/wikipedia/commons/thumb/e/eb/WoW_icon.svg/2048px-WoW_icon.svg.png";
    private final ObtainServerPort obtainServerPort;
    private final SaveServerPort saveServerPort;
    private final RandomString randomString;
    private final PasswordEncoder passwordEncoder;

    public ServerService(ObtainServerPort obtainServerPort, SaveServerPort saveServerPort,
                         @Qualifier("random-string") RandomString randomString, PasswordEncoder passwordEncoder) {
        this.obtainServerPort = obtainServerPort;
        this.saveServerPort = saveServerPort;
        this.randomString = randomString;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ServerDto> findByUserId(Long userId, String transactionId) {
        return obtainServerPort.findByUser(userId, transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    //@Cacheable(value = "server-apikey", key = "#apiKey")
    public ServerModel findByApiKey(String apiKey, String transactionId) {
        return obtainServerPort.findByApiKey(apiKey, transactionId).map(ServerMapper::toModel)
                .orElse(null);
    }

    @Override
    public Optional<ServerEntity> findById(Long id, String transactionId) {
        return obtainServerPort.findById(id, transactionId);
    }

    @Override
    public void create(ServerCreateDto serverCreateDto, Long userId, String transactionId) {

        if (obtainServerPort.findByNameAndExpansion(serverCreateDto.getName(), serverCreateDto.getExpansion(),
                transactionId).isPresent()) {
            throw new InternalException("It is not possible to create or configure a server with because one already " +
                    "exists with the same name and with the same version characteristics.", transactionId);
        }
        try {
            final String apiKey = randomString.nextString();
            final String apiSecret = randomString.nextString();

            byte[] salt = KeyDerivationUtil.generateSalt();
            final String externalPass = serverCreateDto.getExternalPassword();

            final String password = passwordEncoder.encode(serverCreateDto.getPassword());
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
            String encryptedMessage = EncryptionUtil.encrypt(externalPass, derivedKey);

            ServerModel serverDto = ServerModel.builder()
                    .name(serverCreateDto.getName())
                    .emulator(serverCreateDto.getEmulator())
                    .expansion(serverCreateDto.getExpansion())
                    .avatar(AVATAR_SERVER_DEFAULT)
                    .ip(serverCreateDto.getHost())
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .salt(salt)
                    .password(password)
                    .creationDate(LocalDateTime.now())
                    .status(false)
                    .realmlist(serverCreateDto.getRealmlist())
                    .webSite(serverCreateDto.getWebSite())
                    .externalPassword(encryptedMessage)
                    .userId(userId)
                    .externalUsername(serverCreateDto.getExternalUsername())
                    .build();

            saveServerPort.save(ServerMapper.toEntity(serverDto), transactionId);

        } catch (Exception e) {
            LOGGER.error("An error occurred while encrypting data for a new server {}", transactionId);
            throw new InternalException("It was not possible to create a server at this time, an unexpected error has" +
                    " occurred, please contact support", transactionId);
        }

    }

    @Override
    public List<ServerEntity> findByStatusIsTrueServers(String transactionId) {
        return obtainServerPort.findByStatusIsTrue(transactionId);
    }


    @Override
    public List<ServerDto> findByStatusIsTrue(String transactionId) {
        return findByStatusIsTrueServers(transactionId).stream().map(this::mapToModel).toList();
    }

    private ServerDto mapToModel(ServerEntity server) {
        ServerDto serverDto = new ServerDto();
        serverDto.setId(server.getId());
        serverDto.setName(server.getName());
        serverDto.setStatus(server.isStatus());
        serverDto.setEmulator(server.getEmulator());
        serverDto.setExpansion(server.getExpansion());
        serverDto.setCreationDate(server.getCreationDate());
        serverDto.setWebSite(server.getWebSite());
        serverDto.setAvatar(server.getAvatar());
        serverDto.setApiKey(server.getApiKey());
        serverDto.setExpName(Expansion.getById(Integer.parseInt(serverDto.getExpansion())).getDisplayName());
        return serverDto;
    }

    @Override
    public ServerModel findByNameAndVersionAndStatusIsTrue(String name, String version,
                                                           String transactionId) {
        return obtainServerPort.findByNameAndExpansionAndStatusIsTrue(name, version, transactionId)
                .map(ServerMapper::toModel).orElse(null);
    }


}
