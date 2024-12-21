package com.register.wowlibre.infrastructure.repositories.credit_loans;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.time.*;
import java.util.*;

public interface CreditLoansRepository extends CrudRepository<CreditLoansEntity, Long> {

    List<CreditLoansEntity> findByUserId_idAndStatusIsTrue(Long userId);

    Optional<CreditLoansEntity> findByReferenceSerial(String referenceSerial);

    Page<CreditLoansEntity> findByStatusIsTrueAndSendIsFalse(Pageable pageable);

    @Query("SELECT c FROM CreditLoansEntity c WHERE c.status = true AND c.send = true AND c.paymentDate < :currentDate")
    List<CreditLoansEntity> findActiveCreditLoans(@Param("currentDate") LocalDateTime currentDate);


    @Query("SELECT c FROM CreditLoansEntity c WHERE " +
            "( :filter = 'ALL' OR (:filter = 'DEBTOR' AND c.debtToPay > 0) OR (:filter = 'NON_DEBTOR' AND c.debtToPay = 0) ) " +
            "AND c.serverId = :serverId")
    Page<CreditLoansEntity> findByFilterAndSortAndServerId(String filter, Long serverId, Pageable pageable);



}
