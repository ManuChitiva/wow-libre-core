package com.register.wowlibre.infrastructure.repositories.user;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndStatusIsTrue(final String email);

    Optional<UserEntity> findByCellPhoneAndStatusIsTrue(final String cellPhone);

    @Override
    List<UserEntity> findAll();
}
