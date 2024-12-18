package com.register.wowlibre.application.services.security_validation_service;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.port.in.security_validation.*;
import com.register.wowlibre.domain.port.out.user_validation.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class SecurityValidationService implements SecurityValidationPort {
    /**
     * USERS PORT
     **/
    private final ObtainUserValidation obtainUserValidation;
    private final SaveUserValidation saveUserValidation;

    /**
     * EXTERNAL - RANDOM
     **/
    private final RandomString randomString;
    private final RandomString otpRandom;


    public SecurityValidationService(@Qualifier("random-code") RandomString randomString,
                                     @Qualifier("random-send-otp") RandomString otpRandom,
                                     ObtainUserValidation obtainUserValidation, SaveUserValidation saveUserValidation) {
        this.randomString = randomString;
        this.otpRandom = otpRandom;
        this.obtainUserValidation = obtainUserValidation;
        this.saveUserValidation = saveUserValidation;
    }

    @Override
    public String findByCodeEmailValidation(String email, String transactionId) {
        return obtainUserValidation.findByEmail(email, transactionId)
                .map(UserValidationEntity::getCode)
                .orElseThrow(() -> new InternalException("The email validation code cannot be found", transactionId));
    }


    @Override
    public String generateCodeValidationMail(String email, String transactionId) {
        final String code = String.format("%s_%s", email, randomString.nextString());

        UserValidationEntity userValidation = obtainUserValidation
                .findByEmail(email, transactionId)
                .orElseGet(UserValidationEntity::new);

        userValidation.setEmail(email);
        userValidation.setCode(code);
        saveUserValidation.save(userValidation, transactionId);
        return code;
    }


    @Override
    public String generateOtpRecoverAccount(String email, String transactionId) {
        String otp = otpRandom.nextString();

        UserValidationEntity userValidation = obtainUserValidation.findByEmail(email, transactionId)
                .orElseGet(UserValidationEntity::new);

        userValidation.setEmail(email);
        userValidation.setOtp(otp);

        saveUserValidation.save(userValidation, transactionId);

        return otp;
    }

    @Override
    public void resetOtpValidation(String email, String transactionId) {

        UserValidationEntity userValidation = obtainUserValidation
                .findByEmail(email, transactionId)
                .orElseGet(UserValidationEntity::new);
        final String otp = otpRandom.nextString();

        userValidation.setEmail(email);
        userValidation.setOtp(otp);
        saveUserValidation.save(userValidation, transactionId);
    }


    @Override
    public String findByOtpRecoverPassword(String email, String transactionId) {
        return obtainUserValidation.findByEmail(email, transactionId)
                .map(UserValidationEntity::getOtp).orElseThrow(() -> new InternalException("NotFound " +
                        "getCodeEmailRecoverPassword", transactionId));
    }
}
