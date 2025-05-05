package com.register.wowlibre.model;

import com.register.wowlibre.infrastructure.entities.*;

import java.time.*;

public class BaseTest {
    protected UserEntity createSampleUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setCellPhone("1234567890");
        user.setStatus(true);
        user.setVerified(false);
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setCountry("US");
        user.setAvatarUrl("avatar.jpg");
        user.setLanguage("en");
        return user;
    }

}
