package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.server.*;
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
    private final ServerPort serverPort;
    private final I18nService i18nService;

    public TransactionBanks(ObtainCreditLoans obtainCreditLoans, SaveCreditLoans saveCreditLoans,
                            IntegratorPort integratorPort, ServerPort serverPort, I18nService i18nService) {
        this.obtainCreditLoans = obtainCreditLoans;
        this.saveCreditLoans = saveCreditLoans;
        this.integratorPort = integratorPort;
        this.serverPort = serverPort;
        this.i18nService = i18nService;
    }

    @Scheduled(cron = "1 1 * * * *")
    public void sendCreditLoans() {
        String transactionId = "[TransactionBanks] [SendCreditLoans]";
        List<CreditLoansEntity> credits = obtainCreditLoans.creditPendingSend(transactionId);

        credits.forEach(credit -> {
            LOGGER.info("[TransactionBanks] [SendCreditLoans] Sending credit applications CreditId {}", credit.getId());

            Optional<RealmEntity> server = serverPort.findById(credit.getServerId(), transactionId);
            if (server.isPresent()) {

                try {
                    final Long characterId = credit.getCharacterId();
                    final byte[] salt = KeyDerivationUtil.generateSalt();
                    final CharacterDetailDto charactersDto = integratorPort.characters(server.get().getIp(),
                                    server.get().getJwt(), credit.getAccountId(),
                                    credit.getUserId().getId(), transactionId).getCharacters()
                            .stream().filter(character -> character.getId().equals(characterId)).findFirst()
                            .orElse(null);

                    if (charactersDto == null) {
                        LOGGER.error("[TransactionBanks] [SendCreditLoans] Character not found credit: {}",
                                credit.getId());
                        return;
                    }
                    final String characterName = charactersDto.getName();
                    final Locale locale = new Locale(credit.getUserId().getLanguage().toUpperCase());

                    final String command = CommandsCore.sendMoney(characterName, "", getGoblinMessage(characterName, locale),
                            String.valueOf(credit.getAmountTransferred().intValue()));

                    SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(server.get().getApiSecret(), salt);
                    String encryptedMessage = EncryptionUtil.encrypt(command, derivedKey);

                    integratorPort.executeCommand(server.get().getIp(), server.get().getJwt(), encryptedMessage, salt,
                            transactionId);
                    credit.setSend(true);
                    saveCreditLoans.save(credit, transactionId);
                    LOGGER.info("Gold credit sent successfully  ID: {}", credit.getId());
                } catch (Exception e) {
                    LOGGER.error("[TransactionBanks] [SendCreditLoans] An error occurred while sending the requested " +
                            "loan money {}", e.getMessage());
                }
            } else {
                LOGGER.error("[TransactionBanks] [SendCreditLoans]  The server was not found for the requested credit");
            }

        });
    }

    @Scheduled(cron = "1 0 1/6 * * *")
    public void makeCreditCollections() {
        LOGGER.info("[TransactionBanks] [makeCreditCollections] Realizando Cobros");
        String transactionId = "[TransactionBanks] [makeCreditCollections]";
        List<CreditLoansEntity> credits = obtainCreditLoans.creditRequestPending(LocalDateTime.now(), transactionId);

        credits.forEach(credit -> {
            Optional<RealmEntity> server = serverPort.findById(credit.getServerId(), transactionId);

            if (server.isPresent()) {
                try {

                    Double pendingPayment = integratorPort.collectGold(server.get().getIp(), server.get().getJwt(),
                            credit.getUserId().getId(), credit.getDebtToPay(), transactionId);

                    if (pendingPayment <= 0) {
                        credit.setStatus(false);
                    }
                    credit.setDebtToPay(pendingPayment);
                    saveCreditLoans.save(credit, transactionId);
                    LOGGER.info("Successful collection made to the Id {}", credit.getId());
                } catch (Exception e) {
                    LOGGER.error("[TransactionBanks] [makeCreditCollections]  An error has occurred when collecting " +
                            "credit from the customer {}", e.getMessage());
                }
            } else {
                LOGGER.error("[TransactionBanks] [makeCreditCollections]  The server was not found for the requested " +
                        "credit ServerId: {}", credit.getServerId());
            }

        });
    }

    private String getGoblinMessage(String characterName, Locale locale) {
        return i18nService.tr("message-loans-globin", new Object[]{characterName}, locale);
    }
}
