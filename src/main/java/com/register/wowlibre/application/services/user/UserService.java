package com.register.wowlibre.application.services.user;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.account_validation.*;
import com.register.wowlibre.domain.port.in.jwt.*;
import com.register.wowlibre.domain.port.in.mail.*;
import com.register.wowlibre.domain.port.in.rol.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.user.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService implements UserPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String PICTURE_DEFAULT_PROFILE_WEB = "https://i.ibb.co/M8Kfq9X/icon-Default.png";
    private final ObtainUserPort obtainUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtPort jwtPort;
    private final RolPort rolPort;
    private final MailPort mailPort;
    private final AccountValidationPort accountValidationPort;
    private final I18nService i18nService;
    private final RandomString randomString;

    public UserService(ObtainUserPort obtainUserPort, SaveUserPort saveUserPort, PasswordEncoder passwordEncoder,
                       JwtPort jwtPort, RolPort rolPort, MailPort mailPort, AccountValidationPort accountValidationPort,
                       I18nService i18nService, @Qualifier("random-string") RandomString randomString) {
        this.obtainUserPort = obtainUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtPort = jwtPort;
        this.rolPort = rolPort;
        this.mailPort = mailPort;
        this.accountValidationPort = accountValidationPort;
        this.i18nService = i18nService;
        this.randomString = randomString;
    }


    @Override
    public JwtDto create(UserDto userDto, Locale locale, String transactionId) {
        final String email = userDto.getEmail();

        if (findByEmail(userDto.getEmail(), transactionId) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final String passwordEncode = passwordEncoder.encode(userDto.getPassword());

        final RolModel rolModel = rolPort.findByName(Roles.CLIENT.getRoleName(), transactionId);

        if (rolModel == null) {
            LOGGER.error("An error occurred while assigning a role. email {} transactionId: {}", email,
                    transactionId);
            throw new InternalException("An error occurred while assigning a role.", transactionId);
        }

        userDto.setPassword(passwordEncode);
        final UserEntity user = saveUserPort.save(mapToModel(userDto, rolModel), transactionId);

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(rolModel),
                passwordEncode,
                userDto.getEmail(),
                true,
                true,
                true,
                user.getStatus(),
                user.getId(),
                user.getAvatarUrl(),
                userDto.getLanguage()
        );

        final String token = jwtPort.generateToken(customUserDetails);
        final Date expiration = jwtPort.extractExpiration(token);
        final String refreshToken = jwtPort.generateRefreshToken(customUserDetails);

        return new JwtDto(token, refreshToken, expiration, PICTURE_DEFAULT_PROFILE_WEB,
                customUserDetails.getLanguage(), true);
    }


    private UserEntity mapToModel(UserDto userDto, RolModel rolModel) {
        UserEntity user = new UserEntity();
        user.setAvatarUrl(PICTURE_DEFAULT_PROFILE_WEB);
        user.setEmail(userDto.getEmail());
        user.setCountry(userDto.getCountry());
        user.setFirstName(userDto.getFirstName());
        user.setCellPhone(userDto.getCellPhone());
        user.setLanguage(userDto.getLanguage());
        user.setPassword(userDto.getPassword());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setStatus(true);
        user.setVerified(false);
        user.setRolId(RolMapper.toEntity(rolModel));
        return user;
    }

    @Override
    public UserModel findByEmail(String email, String transactionId) {
        return findByEmailEntity(email, transactionId).map(UserEntity::mapToModelEntity).orElse(null);
    }

    @Override
    public UserModel findByPhone(String cellPhone, String transactionId) {
        return obtainUserPort.findByCellPhoneAndStatusIsTrue(cellPhone, transactionId).map(UserEntity::mapToModelEntity)
                .orElse(null);
    }

    @Override
    public Optional<UserEntity> findByUserId(Long userId, String transactionId) {
        return obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId);
    }

    @Override
    public Optional<UserEntity> findByEmailEntity(String email, String transactionId) {
        return obtainUserPort.findByEmailAndStatusIsTrue(email);
    }

    @Override
    public void validationAccount(Long userId, String code, String transactionId) {

        Optional<UserEntity> userFound = findByUserId(userId, transactionId);

        if (userFound.isEmpty() || !userFound.get().getStatus()) {
            throw new InternalException("It was not possible to validate your code, the account is disabled or " +
                    "does not exist.", transactionId);
        }
        if (userFound.get().getVerified()) {
            return;
        }

        UserEntity userModel = userFound.get();

        String obtainedCode = accountValidationPort.retrieveEmailCode(userModel.getEmail(), transactionId);

        if (obtainedCode != null && obtainedCode.equals(code)) {
            userModel.setVerified(true);
            saveUserPort.save(userModel, transactionId);
            accountValidationPort.clearEmailCode(userModel.getEmail(), transactionId);
        } else {
            throw new InternalException("The codes are invalid", transactionId);
        }
    }

    @Override
    public void resetPassword(String email, String transactionId) {
        Optional<UserEntity> account = findByEmailEntity(email, transactionId);

        if (account.isEmpty() || !account.get().getStatus()) {
            throw new InternalException("The email entered does not exist", transactionId);
        }

        Locale locale = new Locale(account.get().getLanguage());
        final String codeOtp = accountValidationPort.generateCodeRecoverAccount(account.get().getEmail(),
                transactionId);
        final String body = i18nService.tr("recovery-password-body", new Object[]{codeOtp.toUpperCase()}, locale);
        final String subject = i18nService.tr("recovery-password-subject", locale);

        mailPort.sendMail(account.get().getEmail(), subject, body, transactionId);
    }

    @Override
    public void validateOtpRecoverPassword(String email, String code, String transactionId) {

        final String codeObtain = accountValidationPort.getCodeEmailRecoverPassword(email, transactionId);

        if (codeObtain == null) {
            throw new InternalException("Expired security code.", transactionId);
        }

        if (!codeObtain.toUpperCase().equals(code)) {
            throw new InternalException("The security code is invalid", transactionId);
        }

        Optional<UserEntity> account = findByEmailEntity(email, transactionId);

        if (account.isEmpty()) {
            throw new InternalException("It was not possible to assign a new password, please contact support",
                    transactionId);
        }

        UserEntity user = account.get();
        String password = randomString.nextString();

        Locale locale = new Locale(account.get().getLanguage());
        final String body = i18nService.tr("message-new-password-body", new Object[]{password}, locale);
        final String subject = i18nService.tr("message-new-password-subject", locale);
        mailPort.sendMail(account.get().getEmail(), subject, body, transactionId);
        user.setPassword(passwordEncoder.encode(password));
        saveUserPort.save(user, transactionId);
    }

    @Override
    public void sendMailValidation(String mail, String transactionId) {
        Optional<UserEntity> account = findByEmailEntity(mail, transactionId);

        if (account.isEmpty()) {
            throw new InternalException("It was not possible to assign a new password, please contact support",
                    transactionId);
        }

        UserEntity user = account.get();

        if (user.getVerified()) {
            return;
        }
        Locale locale = new Locale(account.get().getLanguage());
        final String code = accountValidationPort.generateCodeMail(user.getEmail(), transactionId);

        mailPort.sendCodeMail(user.getEmail(), "Bienvenido, Su cuenta ha sido creada exitosamente, Por favor " +
                "verifique su " +
                "correo", code, locale, transactionId);
    }

    @Override
    public void changePassword(Long userId, String password, String newPassword, String transactionId) {
        Optional<UserEntity> userFound = findByUserId(userId, transactionId);

        if (userFound.isEmpty() || !userFound.get().getStatus()) {
            throw new InternalException("It was not possible to change the password because the account cannot be " +
                    "found", transactionId);
        }

        UserEntity user = userFound.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InternalException("Could not change password", transactionId);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        saveUserPort.save(user, transactionId);

    }


}
