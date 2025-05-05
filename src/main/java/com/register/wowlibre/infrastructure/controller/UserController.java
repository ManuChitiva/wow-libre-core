package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/account")
public class UserController {
    private final UserPort userPort;

    public UserController(UserPort userPort) {
        this.userPort = userPort;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GenericResponse<JwtDto>> create(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE, required = false) Locale locale,
            @RequestBody @Valid UserDto createUser, HttpServletRequest request) {

        final JwtDto jwtDto = userPort.create(createUser, request.getRemoteAddr(), locale, transactionId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponseBuilder<>(jwtDto, transactionId).created().build());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<GenericResponse<UserExistenceDto>> search(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam(name = "email", required = false) final String email,
            @RequestParam(name = "cell_phone", required = false) final String cellPhone) {

        if (email == null && cellPhone == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericResponseBuilder<UserExistenceDto>(transactionId).build());
        }

        boolean exists;

        if (email != null) {
            exists = Optional.ofNullable(userPort.findByEmail(email, transactionId)).isPresent();
        } else {
            exists = Optional.ofNullable(userPort.findByPhone(cellPhone, transactionId)).isPresent();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<UserExistenceDto>(transactionId)
                        .ok(new UserExistenceDto(exists)).build());
    }


    @GetMapping
    public ResponseEntity<GenericResponse<UserDetailDto>> detail(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId) {

        UserDetailDto userResponse = userPort.findByUserId(userId, transactionId)
                .map(model -> UserDetailDto.builder()
                        .id(model.getId())
                        .country(model.getCountry())
                        .dateOfBirth(model.getDateOfBirth())
                        .firstName(model.getFirstName())
                        .lastName(model.getLastName())
                        .cellPhone(model.getCellPhone())
                        .email(model.getEmail())
                        .rolName(model.getRolId().getName())
                        .status(model.getStatus())
                        .verified(model.getVerified())
                        .build()).orElse(null);

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponseBuilder<UserDetailDto>(transactionId).build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<UserDetailDto>(transactionId).ok(userResponse).build());
    }


    @PutMapping("/email/confirmation")
    public ResponseEntity<GenericResponse<Void>> validateEmailCodeForAccount(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestParam final String code) {

        userPort.validateEmailCodeForAccount(userId, code, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @GetMapping("/password-recovery/request")
    public ResponseEntity<GenericResponse<Void>> requestPasswordRecoveryCode(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam final String email) {

        userPort.generateRecoveryCode(email, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PutMapping("/password-recovery/confirm")
    public ResponseEntity<GenericResponse<Void>> validateRecoveryCodeAndResetPassword(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestParam final String email,
            @RequestParam final String code) {

        userPort.resetPasswordWithRecoveryCode(email, code, locale, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PutMapping("/validated-mail/send")
    public ResponseEntity<GenericResponse<Void>> sendValidationEmail(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_EMAIL, required = false) final String email) {

        userPort.sendMailValidation(email, transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @PutMapping(path = "/user-password/change")
    public ResponseEntity<GenericResponse<Void>> newPassword(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_USER_ID) final Long userId,
            @RequestBody @Valid ChangePasswordUserDto changePassword) {

        userPort.changePassword(userId, changePassword.getPassword(),
                changePassword.getNewPassword(), transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }
}
