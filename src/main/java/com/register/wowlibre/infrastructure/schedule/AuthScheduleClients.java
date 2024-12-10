package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.port.in.auth_integrator.*;
import com.register.wowlibre.domain.port.out.server.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.util.*;

@Component
public class AuthScheduleClients {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthScheduleClients.class);

    private final ObtainServerPort obtainServerPort;
    private final AuthIntegratorPort authIntegratorPort;
    private final SaveServerPort saveServerPort;

    public AuthScheduleClients(ObtainServerPort obtainServerPort, AuthIntegratorPort authIntegratorPort,
                               SaveServerPort saveServerPort) {
        this.obtainServerPort = obtainServerPort;
        this.authIntegratorPort = authIntegratorPort;
        this.saveServerPort = saveServerPort;
    }

    @Scheduled(cron = "1 1/50 * * * *")
    public void authServers() {
        LOGGER.info("Init Refresh Jwt Servers [authServers] [AuthScheduleClients]");
        final String transactionId = "Auth-Client";
        List<ServerEntity> servers = obtainServerPort.findByStatusIsTrue(transactionId);

        for (ServerEntity server : servers) {
            try {
                Date expirationDate = server.getExpirationDate();

                if (expirationDate == null || isExpirationDateExpired(expirationDate)) {
                    Date adjustedExpirationDate = expirationDate != null ? adjustExpirationDate(expirationDate) :
                            new Date();

                    if (adjustedExpirationDate.before(new Date())) {

                        byte[] salt = server.getSalt();
                        final String apiSecret = server.getApiSecret();
                        final String password = server.getExternalPassword();

                        SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
                        String decryptPassword = EncryptionUtil.decrypt(password, derivedKey);
                        AuthClientResponse authToken = authIntegratorPort.auth(
                                server.getIp(), server.getExternalUsername(),
                                decryptPassword, transactionId
                        );

                        server.setJwt(authToken.getJwt());
                        server.setExpirationDate(authToken.getExpirationDate());
                        server.setRefreshToken(authToken.getRefreshToken());

                        saveServerPort.save(server, transactionId);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[AuthScheduleClients] [authServers] {}", e.getMessage());
            }
        }

    }

    @Scheduled(cron = "1 1/1 * * * *")
    public void availableServers() {
        final String transactionId = "Auth-Client";
        List<ServerEntity> servers = obtainServerPort.findByStatusIsFalse(transactionId);

        for (ServerEntity server : servers) {
            try {
                byte[] salt = server.getSalt();
                final String password = server.getExternalPassword();
                final String apiSecret = server.getApiSecret();
                final String username = server.getExternalUsername();


                authIntegratorPort.create(server.getIp(), username, password, salt,
                        transactionId);

                SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
                String decryptPassword = EncryptionUtil.decrypt(password, derivedKey);


                AuthClientResponse authToken = authIntegratorPort.auth(
                        server.getIp(), server.getExternalUsername(), decryptPassword, transactionId
                );


                server.setJwt(authToken.getJwt());
                server.setExpirationDate(authToken.getExpirationDate());
                server.setRefreshToken(authToken.getRefreshToken());
                server.setStatus(true);
                saveServerPort.save(server, transactionId);
            } catch (Exception e) {
                LOGGER.error("[AuthScheduleClients] [availableServers] {}", e.getMessage());
            }
        }

    }

    private boolean isExpirationDateExpired(Date expirationDate) {
        return expirationDate.before(new Date());
    }

    private Date adjustExpirationDate(Date expirationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expirationDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

}
