package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.out.credit_loans.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.time.*;
import java.util.*;

@Component
public class TransactionBanks {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionBanks.class);
    private final ObtainCreditLoans obtainCreditLoans;
    private final SaveCreditLoans saveCreditLoans;
    private final IntegratorPort integratorPort;
    private final RealmPort realmPort;
    private final I18nService i18nService;

    public TransactionBanks(ObtainCreditLoans obtainCreditLoans, SaveCreditLoans saveCreditLoans,
                            IntegratorPort integratorPort, RealmPort realmPort, I18nService i18nService) {
        this.obtainCreditLoans = obtainCreditLoans;
        this.saveCreditLoans = saveCreditLoans;
        this.integratorPort = integratorPort;
        this.realmPort = realmPort;
        this.i18nService = i18nService;
    }

    @Scheduled(cron = "1 0/1 * * * *")
    public void sendCreditLoans() {
        String transactionId = "[TransactionBanks] [SendCreditLoans]";
        List<CreditLoansEntity> credits = obtainCreditLoans.creditPendingSend(transactionId);

        credits.forEach(credit -> {
            LOGGER.info("[TransactionBanks] [SendCreditLoans] Sending credit applications CreditId {}", credit.getId());

            Optional<RealmEntity> realm = realmPort.findById(credit.getRealmId(), transactionId);
            if (realm.isPresent()) {
                UserEntity user = credit.getAccountGameId().getUserId();

                try {

                    final Long characterId = credit.getCharacterId();
                    final CharacterDetailDto charactersDto = integratorPort.characters(realm.get().getHost(),
                                    realm.get().getJwt(), credit.getAccountGameId().getAccountId(),
                                    user.getId(), transactionId).getCharacters()
                            .stream().filter(character -> character.getId().equals(characterId)).findFirst()
                            .orElse(null);

                    if (charactersDto == null) {
                        LOGGER.error("[TransactionBanks] [SendCreditLoans] Character not found credit: {}",
                                credit.getId());
                        return;
                    }

                    final String characterName = charactersDto.getName();
                    final Locale locale = new Locale(user.getLanguage());
                    final byte[] salt = KeyDerivationUtil.generateSalt();

                    final String command = CommandsCore.sendMoney(characterName, " ", getGoblinMessage(characterName,
                                    locale),
                            String.valueOf(credit.getAmountTransferred().intValue()));

                    SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(realm.get().getApiSecret(), salt);
                    String encryptedMessage = EncryptionUtil.encrypt(command, derivedKey);

                    integratorPort.executeCommand(realm.get().getHost(), realm.get().getJwt(), encryptedMessage, salt,
                            transactionId);
                    credit.setSend(true);
                    saveCreditLoans.save(credit, transactionId);
                    LOGGER.info("Gold credit sent successfully  ID: {}", credit.getId());
                } catch (Exception e) {
                    LOGGER.error("[TransactionBanks] [SendCreditLoans] An error occurred while sending the requested " +
                            "loan money {}", e.getMessage());
                }
            } else {
                LOGGER.error("[TransactionBanks] [SendCreditLoans]  The realm was not found for the requested credit");
            }

        });
    }

    @Scheduled(cron = "1 0/1 * * * *")
    public void makeCreditCollections() {
        LOGGER.info("[TransactionBanks] [makeCreditCollections] Realizando Cobros");
        String transactionId = "[TransactionBanks] [makeCreditCollections]";
        List<CreditLoansEntity> credits = obtainCreditLoans.creditRequestPending(LocalDateTime.now(), transactionId);

        credits.forEach(credit -> {
            Optional<RealmEntity> server = realmPort.findById(credit.getRealmId(), transactionId);

            if (server.isPresent()) {
                try {

                    Double pendingPayment = integratorPort.collectGold(server.get().getHost(), server.get().getJwt(),
                            credit.getAccountGameId().getUserId().getId(), credit.getDebtToPay(), transactionId);

                    if (pendingPayment <= 0) {
                        credit.setStatus(false);
                    }

                    credit.setDebtToPay(pendingPayment);
                    saveCreditLoans.save(credit, transactionId);
                    LOGGER.info("Successful collection made to the Id {} $[{}]", credit.getId(), pendingPayment);
                } catch (Exception e) {
                    LOGGER.error("[TransactionBanks] [makeCreditCollections]  An error has occurred when collecting " +
                            "credit from the customer {}", e.getMessage());
                }
            } else {
                LOGGER.error("[TransactionBanks] [makeCreditCollections]  The realm was not found for the requested " +
                        "credit ServerId: {}", credit.getRealmId());
            }

        });
    }

    private String getGoblinMessage(String characterName, Locale locale) {
        return i18nService.tr("message-loans-globin", new Object[]{characterName}, locale);
    }
}
