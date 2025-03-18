package com.register.wowlibre.application.services.otp_verification_service;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.port.in.security_validation.*;
import com.register.wowlibre.domain.port.out.otp_verification.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;

@Service
public class OtpVerificationService implements SecurityValidationPort {
    /**
     * USERS PORT
     **/
    private final ObtainOtpVerification obtainOtpVerification;
    private final SaveOtpVerification saveOtpVerification;

    /**
     * EXTERNAL - RANDOM
     **/
    private final RandomString randomString;
    private final RandomString otpRandom;


    public OtpVerificationService(@Qualifier("random-code") RandomString randomString,
                                  @Qualifier("random-send-otp") RandomString otpRandom,
                                  ObtainOtpVerification obtainOtpVerification,
                                  SaveOtpVerification saveOtpVerification) {
        this.randomString = randomString;
        this.otpRandom = otpRandom;
        this.obtainOtpVerification = obtainOtpVerification;
        this.saveOtpVerification = saveOtpVerification;
    }

    @Override
    public String findByCodeEmailValidation(String email, String transactionId) {
        return obtainOtpVerification.findByEmail(email, transactionId)
                .map(OtpVerificationEntity::getCode)
                .orElseThrow(() -> new InternalException("The email validation code cannot be found", transactionId));
    }


    @Override
    public String generateCodeValidationMail(String email, String transactionId) {
        LocalDateTime now = LocalDateTime.now();
        final String code = String.format("%s_%s", email, randomString.nextString());

        OtpVerificationEntity userValidation = obtainOtpVerification
                .findByEmail(email, transactionId)
                .orElseGet(OtpVerificationEntity::new);

        if (userValidation.getCreatedAt() != null &&
                Duration.between(userValidation.getCreatedAt(), now).toMinutes() < 1) {
            throw new InternalException("You must wait one minute to send the validation code again.",
                    transactionId);
        }

        userValidation.setCreatedAt(now);
        userValidation.setEmail(email);
        userValidation.setCode(code);
        saveOtpVerification.save(userValidation, transactionId);
        return code;
    }


    @Override
    public String generateOtpRecoverAccount(String email, String transactionId) {
        String otp = otpRandom.nextString();
        LocalDateTime now = LocalDateTime.now();

        OtpVerificationEntity userValidation = obtainOtpVerification.findByEmail(email, transactionId)
                .orElseGet(OtpVerificationEntity::new);

        if (userValidation.getCreatedAt() != null &&
                Duration.between(userValidation.getCreatedAt(), now).toMinutes() < 1) {
            throw new InternalException("You must wait at least 1 minute before generating a new OTP.",
                    transactionId);
        }

        userValidation.setEmail(email);
        userValidation.setOtp(otp);
        userValidation.setCreatedAt(now);

        saveOtpVerification.save(userValidation, transactionId);

        return otp;
    }

    @Override
    public void resetOtpValidation(String email, String transactionId) {

        OtpVerificationEntity userValidation = obtainOtpVerification
                .findByEmail(email, transactionId)
                .orElseGet(OtpVerificationEntity::new);
        final String otp = otpRandom.nextString();

        userValidation.setEmail(email);
        userValidation.setOtp(otp);
        saveOtpVerification.save(userValidation, transactionId);
    }


    @Override
    public String findByOtpRecoverPassword(String email, String transactionId) {
        return obtainOtpVerification.findByEmail(email, transactionId)
                .map(OtpVerificationEntity::getOtp).orElseThrow(() -> new InternalException("NotFound " +
                        "getCodeEmailRecoverPassword", transactionId));
    }
}
