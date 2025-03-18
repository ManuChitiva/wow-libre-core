package com.register.wowlibre.infrastructure.repositories.user_validation;

import com.register.wowlibre.domain.port.out.otp_verification.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaOtpVerificationAdapter implements SaveOtpVerification, ObtainOtpVerification {
    private final UserValidationRepository userValidationRepository;

    public JpaOtpVerificationAdapter(UserValidationRepository userValidationRepository) {
        this.userValidationRepository = userValidationRepository;
    }

    @Override
    public void save(OtpVerificationEntity otpVerificationEntity, String transactionId) {
        userValidationRepository.save(otpVerificationEntity);
    }

    @Override
    public Optional<OtpVerificationEntity> findByEmail(String email, String transactionId) {
        return userValidationRepository.findByEmail(email);
    }
}
