package com.register.wowlibre.domain.port.in.user;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface UserPort {
    JwtDto create(UserDto accountWebDto, String ip, Locale locale, String transactionId);

    UserModel findByEmail(String email, String transactionId);

    UserModel findByPhone(String cellPhone, String transactionId);

    Optional<UserEntity> findByUserId(Long userId, String transactionId);

    Optional<UserEntity> findByEmailEntity(String email, String transactionId);

    void validateEmailCodeForAccount(Long userId, String code, String transactionId);

    void generateRecoveryCode(String email, String transactionId);

    void resetPasswordWithRecoveryCode(String email, String code, Locale locale, String transactionId);

    void sendMailValidation(String mail, String transactionId);

    void changePassword(Long userId, String password, String newPassword, String transactionId);


}
