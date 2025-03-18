package com.register.wowlibre.domain.port.out.otp_verification;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainOtpVerification {

    Optional<OtpVerificationEntity> findByEmail(String email, String transactionId);
}
