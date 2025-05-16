package com.register.wowlibre.application.services.bank;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.bank.*;
import com.register.wowlibre.domain.port.in.realm_services.*;
import com.register.wowlibre.domain.port.out.credit_loans.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class BankService implements BankPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);

    private final ObtainCreditLoans obtainCreditLoans;
    private final SaveCreditLoans saveCreditLoans;

    private final RealmServicesPort realmServicesPort;
    private final ResourcesPort resourcesPort;
    private final AccountGamePort accountGamePort;
    private final RandomString randomString;

    public BankService(ObtainCreditLoans obtainCreditLoans, SaveCreditLoans saveCreditLoans,
                       RealmServicesPort realmServicesPort,
                       ResourcesPort resourcesPort, AccountGamePort accountGamePort,
                       @Qualifier("reference-serial-bank") RandomString randomString) {
        this.obtainCreditLoans = obtainCreditLoans;
        this.saveCreditLoans = saveCreditLoans;
        this.realmServicesPort = realmServicesPort;
        this.resourcesPort = resourcesPort;
        this.accountGamePort = accountGamePort;
        this.randomString = randomString;
    }


    @Override
    public void applyForLoan(Long userId, Long accountId, Long characterId, Long serverId, Long planId,
                             String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        RealmEntity server = verificationDto.realm();
        AccountGameEntity accountGame = verificationDto.accountGame();

        if (!server.isStatus()) {
            throw new InternalException("Currently the realm is not available to accept loans, contact the " +
                    "administrator.", transactionId);
        }

        RealmServicesModel realmServicesModel =
                realmServicesPort.findByNameAndServerId(RealmServices.BANK, serverId,
                        transactionId);

        if (realmServicesModel == null) {
            throw new InternalException("The realm currently does not have loans configured", transactionId);
        }

        if (realmServicesModel.amount() <= 0) {
            throw new InternalException("There is no money available for loans", transactionId);
        }

        Optional<PlanModel> planSearch =
                resourcesPort.getPlansBank("es", transactionId)
                        .stream().filter(planModel -> planModel.id().equals(planId)).findFirst();

        if (planSearch.isEmpty()) {
            LOGGER.error("The requested plan is not available.");
            throw new InternalException("The requested plan is not available.", transactionId);
        }

        PlanModel plan = planSearch.get();

        if (!obtainCreditLoans.findByAccountGameAndStatusIsTrue(accountGame).isEmpty()) {
            throw new InternalException("You already have a loan.", transactionId);
        }

        int monthPaymentPeriod = plan.monthPaymentPeriod();

        final double cost = calculateMonthlyPayment(plan.gold(), plan.interest(), monthPaymentPeriod);

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime futurePaymentDate = currentDate.plusMonths(monthPaymentPeriod);

        CreditLoansEntity creditLoansEntity = new CreditLoansEntity();
        creditLoansEntity.setCharacterId(characterId);
        creditLoansEntity.setStatus(true);
        creditLoansEntity.setAccountGameId(verificationDto.accountGame());
        creditLoansEntity.setInterests(plan.interest());
        creditLoansEntity.setRealmId(serverId);
        creditLoansEntity.setPaymentDate(futurePaymentDate);
        creditLoansEntity.setDebtToPay(cost);
        creditLoansEntity.setAmountTransferred(plan.gold());
        creditLoansEntity.setReferenceSerial(randomString.nextString());
        creditLoansEntity.setSend(false);

        realmServicesPort.updateAmount(realmServicesModel.id(), realmServicesModel.amount() - 1, transactionId);
        saveCreditLoans.save(creditLoansEntity, transactionId);
    }

    @Override
    public List<ServerAvailableBankDto> serverAvailableLoan(String transactionId) {
        return realmServicesPort.findByServersAvailableLoa(transactionId)
                .stream().map(server -> new ServerAvailableBankDto(server.serverId(), server.serverName())).toList();
    }

    private double calculateMonthlyPayment(double cost, int interest, int paymentMonth) {
        double interestRate = interest / 100.0;
        double monthlyInterest = cost * interestRate;
        return (cost / paymentMonth) + monthlyInterest;
    }
}
