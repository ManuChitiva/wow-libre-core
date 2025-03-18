package com.register.wowlibre.infrastructure.repositories.user_validation;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface UserValidationRepository extends CrudRepository<OtpVerificationEntity, Long> {
    Optional<OtpVerificationEntity> findByEmail(String email);
}
