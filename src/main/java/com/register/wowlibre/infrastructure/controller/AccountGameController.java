package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.account_game.AccountsGameDto;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/account/game")
public class AccountGameController {
    private final AccountGamePort accountGamePort;

    public AccountGameController(AccountGamePort accountGamePort) {
        this.accountGamePort = accountGamePort;
    }

    @GetMapping(path = "/available")
    public ResponseEntity<GenericResponse<AccountsGameDto>> accounts(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam final int page,
            @RequestParam final int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String server) {

        AccountsGameDto accounts = accountGamePort.accounts(userId, page, size, username, server, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).ok().build());
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GenericResponse<Void>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid CreateAccountGameDto account) {

        accountGamePort.create(userId, account.getServerName(), account.getExpansion(), account.getUsername(),
                account.getPassword(), transactionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<Void>(transactionId).created().build());
    }


    @GetMapping
    public ResponseEntity<GenericResponse<AccountsGameDto>> accountsByRealm(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam(name = PARAM_SERVER_ID) Long serverId) {

        AccountsGameDto accounts = accountGamePort.accounts(userId, serverId, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(accounts, transactionId).ok().build());
    }


    @GetMapping(path = "/{account_id}/{server_id}")
    public ResponseEntity<GenericResponse<AccountGameDetailDto>> account(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @PathVariable final Long account_id,
            @PathVariable final Long server_id) {

        final AccountGameDetailDto account = accountGamePort.account(userId, account_id, server_id, transactionId);

        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponseBuilder<AccountGameDetailDto>(transactionId).ok(account).build());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
