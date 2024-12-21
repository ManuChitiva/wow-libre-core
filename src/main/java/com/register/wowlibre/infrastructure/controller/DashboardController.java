package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.port.in.dashboard.*;
import com.register.wowlibre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardPort dashboardPort;

    public DashboardController(DashboardPort dashboardPort) {
        this.dashboardPort = dashboardPort;
    }

    @GetMapping("/credit/loans")
    public ResponseEntity<GenericResponse<LoansDto>> creditsLoans(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam final int size,
            @RequestParam final int page,
            @RequestParam final String filter,
            @RequestParam final boolean asc) {

        LoansDto loansDto = dashboardPort.creditLoans(userId, serverId, size, page, filter, asc, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(loansDto, transactionId).ok().build());
    }


    @PutMapping("/credit/loans")
    public ResponseEntity<GenericResponse<Void>> enableLoan(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam final Double loans,
            @RequestParam final String service) {

        dashboardPort.enableLoan(userId, serverId, loans, service, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }


    @GetMapping("/credit/loans/data")
    public ResponseEntity<GenericResponse<Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>>>> getLoansForLineChart(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId) {

        Map<Integer, Map<String, Map<Integer, Map<String, Integer>>>> loansDto =
                dashboardPort.groupLoansAndPaymentsByDate(userId, serverId, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(loansDto, transactionId).ok().build());
    }


    @GetMapping("/accounts")
    public ResponseEntity<GenericResponse<AccountsGameDto>> accountsServer(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_SERVER_ID) final Long serverId,
            @RequestParam final int size,
            @RequestParam final int page,
            @RequestParam final String filter) {

        AccountsGameDto accounts =
                dashboardPort.accountsServer(userId, serverId, filter, size, page, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).ok().build());
    }
}
