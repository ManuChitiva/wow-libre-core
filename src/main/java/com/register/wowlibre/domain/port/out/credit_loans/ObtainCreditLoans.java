package com.register.wowlibre.domain.port.out.credit_loans;

import com.register.wowlibre.infrastructure.entities.*;

import java.time.*;
import java.util.*;

public interface ObtainCreditLoans {
    List<CreditLoansEntity> findByUserIdAndStatusIsTrue(Long userId);

    Optional<CreditLoansEntity> findByReferenceSerial(String referenceSerial);

    List<CreditLoansEntity> creditPendingSend(String transactionId);

    List<CreditLoansEntity> creditRequestPending(LocalDateTime localDateTime, String transactionId);

    List<CreditLoansEntity> findByServerIdAndPagination(Long serverId, int size, int page, String filter, boolean asc,
                                                        String transactionId);
}
