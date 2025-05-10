package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.port.in.machine.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    private final MachinePort machinePort;

    public MachineController(MachinePort machinePort) {
        this.machinePort = machinePort;
    }

    @GetMapping("/points")
    public ResponseEntity<GenericResponse<MachineDetailDto>> points(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_ACCOUNT_ID) final Long accountId,
            @RequestParam(name = PARAM_SERVER_ID) final Long realmId) {

        MachineDetailDto response = machinePort.points(userId, accountId, realmId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(response, transactionId).ok().build());
    }

    @PostMapping
    public ResponseEntity<GenericResponse<MachineDto>> machine(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestBody @Valid ClaimMachineDto request) {

        MachineDto response = machinePort.evaluate(userId, request.getAccountId(), request.getCharacterId(),
                request.getServerId(), locale,
                transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(response, transactionId).ok().build());
    }

}
