package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.teleport.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.teleport.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.HEADER_TRANSACTION_ID;
import static com.register.wowlibre.domain.constant.Constants.HEADER_USER_ID;

@RestController
@RequestMapping("/api/teleport")
public class TeleportController {
    private final TeleportPort teleportPort;

    public TeleportController(TeleportPort teleportPort) {
        this.teleportPort = teleportPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<TeleportModel>>> all(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam final Long raceId,
            @RequestParam final Long realmId) {
        List<TeleportModel> teleports = teleportPort.findByAll(realmId, raceId, transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<List<TeleportModel>>(transactionId).ok(teleports).build());
    }


    @PostMapping("/character")
    public ResponseEntity<GenericResponse<Void>> character(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid CharacterTeleportDto teleportDto) {
        teleportPort.teleport(teleportDto.getTeleportId(), userId, teleportDto.getAccountId(),
                teleportDto.getCharacterId(), teleportDto.getRealmId(), transactionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Void>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid TeleportDto teleportDto) {
        teleportPort.save(teleportDto, transactionId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }
}
