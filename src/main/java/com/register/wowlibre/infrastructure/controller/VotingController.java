package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.voting_platforms.*;
import com.register.wowlibre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/voting")
public class VotingController {
    private final VotingPlatformsPort votingPlatformsPort;

    public VotingController(VotingPlatformsPort votingPlatformsPort) {
        this.votingPlatformsPort = votingPlatformsPort;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<VotingPlatformsModel>>> getAllActiveVotingPlatforms(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {

        List<VotingPlatformsModel> platforms = votingPlatformsPort.findAllActiveVotingPlatforms(null, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(platforms, transactionId).ok().build());
    }

    @GetMapping("/logged")
    public ResponseEntity<GenericResponse<List<VotingPlatformsModel>>> getAllActiveVotingPlatformsLogged(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        List<VotingPlatformsModel> platforms = votingPlatformsPort.findAllActiveVotingPlatforms(userId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(platforms, transactionId).ok().build());
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<Void>> createVotingPlatform(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam String name,
            @RequestParam String imgUrl,
            @RequestParam String postbackUrl,
            @RequestParam String allowedHost) {
        votingPlatformsPort.createVotingPlatform(name, imgUrl, postbackUrl, allowedHost, transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(null, transactionId).ok().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> updateVotingPlatform(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String imgUrl,
            @RequestParam String postbackUrl,
            @RequestParam String allowedHost) {
        votingPlatformsPort.updateVotingPlatform(id, name, imgUrl, postbackUrl, allowedHost, transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(null, transactionId).ok().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteVotingPlatform(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @PathVariable Long id) {
        votingPlatformsPort.deleteVotingPlatform(id, transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(null, transactionId).ok().build());
    }

    @GetMapping("/postback")
    public ResponseEntity<GenericResponse<Void>> postback(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam("p_resp") String parameter,
            @RequestParam("ip") String ipUsuario) {

        votingPlatformsPort.postbackVotingPlatform(parameter, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(null, transactionId).ok().build());
    }
}
