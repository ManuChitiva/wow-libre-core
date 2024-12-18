package com.register.wowlibre.domain.port.in.security_validation;

public interface SecurityValidationPort {
    String findByCodeEmailValidation(String email, String transactionId);

    String generateCodeValidationMail(String email, String transactionId);

    String generateOtpRecoverAccount(String mail, String transactionId);

    void resetOtpValidation(String email, String transactionId);

    String findByOtpRecoverPassword(String email, String transactionId);
}
