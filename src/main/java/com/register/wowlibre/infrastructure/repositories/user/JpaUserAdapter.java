package com.register.wowlibre.infrastructure.repositories.user;

import com.register.wowlibre.domain.port.out.user.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaUserAdapter implements ObtainUserPort, SaveUserPort {
    private final UserRepository userRepository;

    public JpaUserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findByEmailAndStatusIsTrue(String email) {
        return userRepository.findByEmailAndStatusIsTrue(email);
    }

    @Override
    public Optional<UserEntity> findByCellPhoneAndStatusIsTrue(String cellPhone, String transactionId) {
        return userRepository.findByCellPhoneAndStatusIsTrue(cellPhone);
    }

    @Override
    //@Cacheable(value = "findByUserIdAndStatusIsTrue", key = "#userId")
    public Optional<UserEntity> findByUserIdAndStatusIsTrue(Long userId, String transactionId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity save(UserEntity user, String email) {
        return userRepository.save(user);
    }
}
