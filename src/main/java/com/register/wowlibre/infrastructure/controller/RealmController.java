package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/realm")
public class RealmController {
    private final RealmPort realmPort;

    public RealmController(RealmPort realmPort) {
        this.realmPort = realmPort;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GenericResponse<Void>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid RealmCreateDto realmCreateDto) {

        realmPort.create(realmCreateDto, userId, transactionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }

    @GetMapping("/")
    public ResponseEntity<GenericResponse<AssociatedRealm>> realms(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        List<RealmDto> realms = realmPort.findAll(transactionId);

        if (realms == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new GenericResponseBuilder<AssociatedRealm>(transactionId).notContent().build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(new AssociatedRealm(realms, realms.size()), transactionId).ok().build());
    }

    @GetMapping("/key")
    public ResponseEntity<GenericResponse<RealmModel>> apiKey(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "api_key") String apiKey) {

        final RealmModel server = realmPort.findByApiKey(apiKey, transactionId);

        if (server == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new GenericResponseBuilder<RealmModel>(transactionId).notContent().build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(server, transactionId).ok().build());
    }


    @GetMapping
    public ResponseEntity<GenericResponse<List<RealmDto>>> servers(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        final List<RealmDto> serverList = realmPort.findByStatusIsTrue(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(serverList, transactionId).ok().build());
    }

    @GetMapping("/vdp")
    public ResponseEntity<GenericResponse<ServerVdpDto>> vdpServer(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "expansion") Integer expansionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        final ServerVdpDto server = realmPort.findByServerNameAndExpansion(name, expansionId, locale,
                transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(server, transactionId).ok().build());
    }

}
