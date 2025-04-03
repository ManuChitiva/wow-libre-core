package com.register.wowlibre.domain.port.out.user;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainUserPort {
    Optional<UserEntity> findByEmailAndStatusIsTrue(String email);

    Optional<UserEntity> findByCellPhoneAndStatusIsTrue(String cellPhone, String transactionId);

    Optional<UserEntity> findByUserIdAndStatusIsTrue(Long userId, String transactionId);

    List<UserEntity> findAll();
}
