package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.port.in.auth_integrator.*;
import com.register.wowlibre.domain.port.out.realm.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.util.*;

@Component
public class RealmsSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealmsSchedule.class);

    private final ObtainRealmPort obtainRealmPort;
    private final SaveRealmPort saveRealmPort;
    private final AuthIntegratorPort authIntegratorPort;
    private final RandomString randomString;

    public RealmsSchedule(ObtainRealmPort obtainRealmPort, AuthIntegratorPort authIntegratorPort,
                          SaveRealmPort saveRealmPort, @Qualifier("reset-password-string") RandomString randomString) {
        this.obtainRealmPort = obtainRealmPort;
        this.authIntegratorPort = authIntegratorPort;
        this.saveRealmPort = saveRealmPort;
        this.randomString = randomString;
    }

    @Scheduled(cron = "1 0/1 * * * *")
    public void LoginRealm() {
        final String transactionId = "[RealmsSchedule][LoginRealm]";
        List<RealmEntity> realms = obtainRealmPort.findByStatusIsTrue(transactionId);

        for (RealmEntity realm : realms) {
            LOGGER.info("[RealmsSchedule][LoginRealm] Automatic start of JWT refresh  {} ", realm.getName());

            try {
                Date expirationDate = realm.getExpirationDate();

                if (expirationDate == null || isExpirationDateExpired(expirationDate)) {
                    Date adjustedExpirationDate = expirationDate != null ? expirationDate :
                            new Date();

                    if (adjustedExpirationDate.before(new Date())) {

                        byte[] salt = realm.getSalt();
                        final String apiSecret = realm.getApiSecret();
                        final String password = realm.getExternalPassword();

                        SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, salt);
                        String decryptPassword = EncryptionUtil.decrypt(password, derivedKey);
                        AuthClientResponse authToken = authIntegratorPort.auth(
                                realm.getHost(), realm.getExternalUsername(),
                                decryptPassword, transactionId
                        );

                        realm.setJwt(authToken.getJwt());
                        realm.setExpirationDate(authToken.getExpirationDate());
                        realm.setRefreshToken(authToken.getRefreshToken());

                        saveRealmPort.save(realm, transactionId);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[RealmsSchedule][LoginRealm] Error: [{}]", e.getMessage());
            }
        }

    }

    @Scheduled(cron = "1 0/1 * * * *")
    public void vinculatedRealm() {
        final String transactionId = "[RealmsSchedule][vinculatedRealm]";
        List<RealmEntity> servers = obtainRealmPort.findByStatusIsFalseAndRetry(5L, transactionId);

        for (RealmEntity server : servers) {
            LOGGER.info("[RealmsSchedule][vinculatedRealm] Start authentication generation process {} ",
                    server.getName());

            try {
                byte[] salt = server.getSalt();
                final String gmUsername = server.getExternalUsername();
                final String usernameRealm = String.format("%s-%s", server.getName(),
                        server.getExpansionId());

                final String gmPassword = server.getExternalPassword();
                final String passwordRealm = randomString.nextString();

                // CREATE USER REALM
                authIntegratorPort.create(server.getHost(), usernameRealm, passwordRealm, salt,
                        server.getApiKey(), server.getEmulator(), server.getExpansionId(), gmUsername, gmPassword,
                        transactionId);

                // LOGIN
                AuthClientResponse authToken = authIntegratorPort.auth(
                        server.getHost(), usernameRealm, passwordRealm, transactionId
                );

                server.setJwt(authToken.getJwt());
                server.setExpirationDate(adjustExpirationDate(authToken.getExpirationDate()));
                server.setRefreshToken(authToken.getRefreshToken());
                server.setStatus(true);
                server.setRetry(0);
                saveRealmPort.save(server, transactionId);
                LOGGER.info("The kingdom is linked correctly {} ", server.getName());
            } catch (Exception e) {
                LOGGER.error("[RealmsSchedule][vinculatedRealm] an unexpected error has occurred Error: {}",
                        e.getMessage());
                server.setRetry(server.getRetry() == null ? 0 : server.getRetry() + 1);
                saveRealmPort.save(server, transactionId);
            }
        }

    }

    private boolean isExpirationDateExpired(Date expirationDate) {
        return expirationDate.before(new Date());
    }

    private Date adjustExpirationDate(Date expirationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expirationDate);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        return calendar.getTime();
    }

}
