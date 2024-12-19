package com.register.wowlibre.domain.port.out.security_validation;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainUserValidation {

    Optional<UserValidationEntity> findByEmail(String email, String transactionId);
}
