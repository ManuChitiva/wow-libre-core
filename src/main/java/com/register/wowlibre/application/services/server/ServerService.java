package com.register.wowlibre.application.services.server;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.server.*;
import com.register.wowlibre.domain.port.in.server_details.*;
import com.register.wowlibre.domain.port.in.server_events.*;
import com.register.wowlibre.domain.port.in.server_resources.*;
import com.register.wowlibre.domain.port.in.user.*;
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
import java.util.stream.*;

@Repository
public class ServerService implements ServerPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerService.class);

    private static final String AVATAR_SERVER_DEFAULT = "https://upload.wikimedia" +
            ".org/wikipedia/commons/thumb/e/eb/WoW_icon.svg/2048px-WoW_icon.svg.png";
    private final ObtainServerPort obtainServerPort;
    private final SaveServerPort saveServerPort;
    private final RandomString randomString;
    private final PasswordEncoder passwordEncoder;
    private final UserPort userPort;
    private final ObtainServerDetailsPort obtainServerDetailsPort;

    private final IntegratorPort integratorPort;
    private final ServerEventsPort serverEventsPort;
    private final ServerResourcesPort serverResourcesPort;
    private final I18nService i18nService;

    public ServerService(ObtainServerPort obtainServerPort, SaveServerPort saveServerPort,
                         @Qualifier("random-string") RandomString randomString, PasswordEncoder passwordEncoder,
                         UserPort userPort, ObtainServerDetailsPort obtainServerDetailsPort,
                         IntegratorPort integratorPort, ServerEventsPort serverEventsPort,
                         ServerResourcesPort serverResourcesPort, I18nService i18nService) {
        this.obtainServerPort = obtainServerPort;
        this.saveServerPort = saveServerPort;
        this.randomString = randomString;
        this.passwordEncoder = passwordEncoder;
        this.userPort = userPort;
        this.obtainServerDetailsPort = obtainServerDetailsPort;
        this.integratorPort = integratorPort;
        this.serverEventsPort = serverEventsPort;
        this.serverResourcesPort = serverResourcesPort;
        this.i18nService = i18nService;
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
    public Optional<RealmEntity> findById(Long id, String transactionId) {
        return obtainServerPort.findById(id, transactionId);
    }

    @Override
    public void create(ServerCreateDto serverCreateDto, Long userId, String transactionId) {

        if (obtainServerPort.findByNameAndExpansion(serverCreateDto.getName(), serverCreateDto.getExpansion(),
                transactionId).isPresent()) {
            throw new InternalException("It is not possible to create or configure a server with because one already " +
                    "exists with the same name and with the same version characteristics.", transactionId);
        }

        if (obtainServerPort.findByUser(userId, transactionId).size() >= 4) {
            throw new InternalException("Currently it is not possible to link more than two servers", transactionId);
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
                    .type(serverCreateDto.getType())
                    .apiSecret(apiSecret)
                    .salt(salt)
                    .password(password)
                    .creationDate(LocalDateTime.now())
                    .retry(0)
                    .status(false)
                    .realmlist(serverCreateDto.getRealmlist())
                    .webSite(serverCreateDto.getWebSite())
                    .externalPassword(encryptedMessage)
                    .userId(userId)
                    .externalUsername(serverCreateDto.getExternalUsername())
                    .build();

            userPort.updateRol(Rol.SERVER, userId, transactionId);
            saveServerPort.save(ServerMapper.toEntity(serverDto), transactionId);

        } catch (Exception e) {
            LOGGER.error("An error occurred while encrypting data for a new server {}", transactionId);
            throw new InternalException("It was not possible to create a server at this time, an unexpected error has" +
                    " occurred, please contact support", transactionId);
        }

    }

    @Override
    public List<RealmEntity> findByStatusIsTrueServers(String transactionId) {
        return obtainServerPort.findByStatusIsTrue(transactionId);
    }

    @Override
    public Optional<RealmEntity> findByIdAndUserId(Long id, Long userId, String transactionId) {
        return obtainServerPort.findAndIdByUser(id, userId, transactionId);
    }

    @Override
    public ServerVdpDto findByServerNameAndExpansion(String name, String expansion, Locale locale,
                                                     String transactionId) {

        RealmEntity serverModel = obtainServerPort.findByNameAndExpansionAndStatusIsTrue(name, expansion,
                transactionId).orElse(null);

        if (serverModel == null) {
            throw new InternalException("", transactionId);
        }


        List<ServerDetailsEntity> serverDetails = obtainServerDetailsPort.findByServerId(serverModel, transactionId);

        Map<String, String> serverDetailsMap = (serverDetails == null) ?
                Collections.emptyMap() :
                serverDetails.stream()
                        .collect(Collectors.toMap(ServerDetailsEntity::getKey, ServerDetailsEntity::getValue,
                                (existing, replacement) -> existing));

        List<ServerResourcesEntity> serverResource = serverResourcesPort.findByServerId(serverModel, transactionId);

        Map<ResourceType, ServerResourcesEntity> resourceMap = serverResource.stream()
                .collect(Collectors.toMap(ServerResourcesEntity::getResourceType,
                        resource -> resource, (a, b) -> a));

        DashboardMetricsResponse dashboard = integratorPort.dashboard(serverModel.getIp(),
                serverModel.getJwt(), transactionId);

        List<ServerVdpDto.Event> events = serverEventsPort.findByServerId(serverModel, transactionId).stream()
                .map(this::buildEventServerVdp).toList();

        List<ServerVdpDto.Card> cards = new ArrayList<>();
        cards.add(buildCardServerVdp(1, String.valueOf(dashboard.getOnlineUsers()), 1, i18nService.tr("card-users" +
                "-online", locale)));
        cards.add(buildCardServerVdp(2, String.valueOf(dashboard.getTotalUsers()), 2, i18nService.tr("card-total" +
                "-users", locale)));
        cards.add(buildCardServerVdp(3, String.valueOf(dashboard.getTotalGuilds()), 3, i18nService.tr("card-total" +
                "-guild", locale)));

        return ServerVdpDto.builder()
                .logo(Optional.ofNullable(resourceMap.get(ResourceType.LOGO))
                        .map(ServerResourcesEntity::getUrl)
                        .orElse(null))
                .headerLeftImg(Optional.ofNullable(resourceMap.get(ResourceType.HEADER_LEFT))
                        .map(ServerResourcesEntity::getUrl)
                        .orElse(null))
                .headerRightImg(Optional.ofNullable(resourceMap.get(ResourceType.HEADER_RIGHT))
                        .map(ServerResourcesEntity::getUrl)
                        .orElse(null))
                .headerCenterImg(Optional.ofNullable(resourceMap.get(ResourceType.HEADER_CENTER))
                        .map(ServerResourcesEntity::getUrl)
                        .orElse(null))
                .youtubeUrl(Optional.ofNullable(resourceMap.get(ResourceType.YOUTUBE_URL))
                        .map(ServerResourcesEntity::getUrl)
                        .orElse(null))
                .name(serverModel.getName())
                .type(serverModel.getType())
                .disclaimer(serverModel.getDisclaimer())
                .information(serverDetailsMap)
                .cards(cards)
                .url(serverModel.getWebSite())
                .events(events)
                .realmlist(serverModel.getRealmlist()).build();
    }

    private ServerVdpDto.Card buildCardServerVdp(int id, String value, int iconId, String description) {
        return new ServerVdpDto.Card(id, value, iconId, description);
    }

    private ServerVdpDto.Event buildEventServerVdp(ServerEventsEntity events) {
        return new ServerVdpDto.Event(events.getId(), events.getImg(), events.getTitle(), events.getDescription(),
                events.getDisclaimer());
    }

    @Override
    public List<ServerDto> findByStatusIsTrue(String transactionId) {
        return findByStatusIsTrueServers(transactionId).stream().map(this::mapToModel).toList();
    }

    @Override
    public ServerModel findByNameAndVersionAndStatusIsTrue(String name, String version,
                                                           String transactionId) {
        return obtainServerPort.findByNameAndExpansionAndStatusIsTrue(name, version, transactionId)
                .map(ServerMapper::toModel).orElse(null);
    }

    private ServerDto mapToModel(RealmEntity server) {
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

}
