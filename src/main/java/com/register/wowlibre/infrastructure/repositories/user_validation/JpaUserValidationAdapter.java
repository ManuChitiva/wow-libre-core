package com.register.wowlibre.infrastructure.repositories.user_validation;

import com.register.wowlibre.domain.port.out.security_validation.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaUserValidationAdapter implements SaveUserValidation, ObtainUserValidation {
    private final UserValidationRepository userValidationRepository;

    public JpaUserValidationAdapter(UserValidationRepository userValidationRepository) {
        this.userValidationRepository = userValidationRepository;
    }

    @Override
    public void save(UserValidationEntity userValidationEntity, String transactionId) {
        userValidationRepository.save(userValidationEntity);
    }

    @Override
    public Optional<UserValidationEntity> findByEmail(String email, String transactionId) {
        return userValidationRepository.findByEmail(email);
    }
}
