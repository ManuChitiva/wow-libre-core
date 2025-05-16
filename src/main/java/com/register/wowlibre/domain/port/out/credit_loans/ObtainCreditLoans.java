package com.register.wowlibre.domain.port.out.credit_loans;

import com.register.wowlibre.infrastructure.entities.*;

import java.time.*;
import java.util.*;

public interface ObtainCreditLoans {
    List<CreditLoansEntity> findByAccountGameAndStatusIsTrue(AccountGameEntity accountGame);

    Optional<CreditLoansEntity> findByReferenceSerial(String referenceSerial);

    List<CreditLoansEntity> creditPendingSend(String transactionId);

    List<CreditLoansEntity> creditRequestPending(LocalDateTime localDateTime, String transactionId);

    List<CreditLoansEntity> findByRealmIdAndPagination(Long realmId, int size, int page, String filter, boolean asc,
                                                       String transactionId);
}
