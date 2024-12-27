package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.port.in.transaction.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionPort transactionPort;

    public TransactionController(TransactionPort transactionPort) {
        this.transactionPort = transactionPort;
    }

    @PostMapping("/purchase")
    public ResponseEntity<GenericResponse<Void>> sendItems(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateTransactionItemsDto request) {

        transactionPort.purchase(request.getServerId(), request.getUserId(), request.getAccountId(),
                request.getReference(),
                request.getItems(), request.getAmount(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping("/subscription-benefits")
    public ResponseEntity<GenericResponse<Void>> sendSubscriptionBenefits(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid SubscriptionBenefitsDto request) {

        transactionPort.sendSubscriptionBenefits(request.getServerId(), request.getUserId(), request.getAccountId(),
                request.getCharacterId(),
                request.getItems(), request.getBenefitType(), request.getAmount(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @GetMapping("/promotions")
    public ResponseEntity<GenericResponse<PromotionsDto>> promotions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam(name = PARAM_CHARACTER_ID) final Long characterId,
            @RequestParam(name = "class_id") final Long classId) {

        PromotionsDto promotionsDto = transactionPort.getPromotions(serverId, userId, accountId,
                characterId, classId, locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(promotionsDto, transactionId).ok().build());
    }

    @PostMapping("/claim-promotions")
    public ResponseEntity<GenericResponse<Void>> claimPromotions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid ClaimPromoDto request) {

        transactionPort.claimPromotion(request.getServerId(), userId,
                request.getAccountId(),
                request.getCharacterId(), request.getPromotionId(), locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
