package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.application.services.realm_advertising.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/realm/advertising")
public class RealmAdvertisingController {

    private final RealmAdvertisingService realmAdvertisingService;

    public RealmAdvertisingController(RealmAdvertisingService realmAdvertisingService) {
        this.realmAdvertisingService = realmAdvertisingService;
    }

    @GetMapping("/{realmId}")
    public ResponseEntity<GenericResponse<RealmAdvertisingModel>> getByRealmId(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @PathVariable Long realmId) {

        RealmAdvertisingModel realmAdvertising = realmAdvertisingService.getRealmAdvertisingById(realmId,
                locale.getLanguage(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(realmAdvertising, transactionId).ok().build());
    }

    @PostMapping("/{realmId}")
    public ResponseEntity<GenericResponse<Void>> save(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody RealmAdvertisingDto dto,
            @PathVariable Long realmId) {
        realmAdvertisingService.save(dto, realmId, transactionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @GetMapping("/language")
    public ResponseEntity<GenericResponse<List<RealmAdvertisingModel>>> findByLanguage(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>
                        (realmAdvertisingService.findByRealmsByLanguage(
                                locale.getLanguage(), transactionId), transactionId).ok().build());
    }
}
