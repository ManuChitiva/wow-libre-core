package com.register.wowlibre.domain.port.out.otp_verification;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveOtpVerification {
    void save(OtpVerificationEntity otpVerificationEntity, String transactionId);
}
