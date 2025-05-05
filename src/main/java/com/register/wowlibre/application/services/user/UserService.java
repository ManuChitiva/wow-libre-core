package com.register.wowlibre.application.services.user;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.google.*;
import com.register.wowlibre.domain.port.in.jwt.*;
import com.register.wowlibre.domain.port.in.mail.*;
import com.register.wowlibre.domain.port.in.rol.*;
import com.register.wowlibre.domain.port.in.security_validation.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.user.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.config.*;
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
    private static final String PICTURE_DEFAULT_PROFILE_WEB = "https://static.wixstatic" +
            ".com/media/5dd8a0_32431551f29b4644a774d8c55d2666fd~mv2.webp";

    /**
     * USERS PORT
     **/
    private final ObtainUserPort obtainUserPort;
    private final SaveUserPort saveUserPort;
    /**
     * EXTERNAL
     **/
    private final PasswordEncoder passwordEncoder;
    private final I18nService i18nService;
    private final RandomString randomString;
    /**
     * Rol Port
     **/
    private final RolPort rolPort;
    /**
     * MAILS - JWT SECURITY
     **/
    private final JwtPort jwtPort;
    private final SecurityValidationPort securityValidationPort;
    private final MailPort mailPort;
    /**
     * VERIFY CAPCHAT PORT
     **/
    private final GooglePort googlePort;
    /**
     * Configurations
     **/
    private final Configurations configurations;

    public UserService(ObtainUserPort obtainUserPort, SaveUserPort saveUserPort, PasswordEncoder passwordEncoder,
                       JwtPort jwtPort, RolPort rolPort, MailPort mailPort,
                       SecurityValidationPort securityValidationPort,
                       I18nService i18nService, @Qualifier("reset-password-string") RandomString randomString,
                       GooglePort googlePort, Configurations configurations) {
        this.obtainUserPort = obtainUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtPort = jwtPort;
        this.rolPort = rolPort;
        this.mailPort = mailPort;
        this.securityValidationPort = securityValidationPort;
        this.i18nService = i18nService;
        this.randomString = randomString;
        this.googlePort = googlePort;
        this.configurations = configurations;
    }


    @Override
    public JwtDto create(UserDto userDto, String ip, Locale locale, String transactionId) {

        if (!googlePort.verifyCaptcha(configurations.getGoogleSecret(), userDto.getToken(), ip,
                transactionId)) {
            LOGGER.error("[UserService] [create] An error occurred while verifying the captcha. -  [transactionId: " +
                    "{}]", transactionId);
            throw new InternalException("The captcha is invalid", transactionId);
        }

        final String email = userDto.getEmail();

        if (findByEmail(email, transactionId) != null) {
            throw new FoundException("There is already a registered client with this data", transactionId);
        }

        final RolModel rolModel = rolPort.findByName(Rol.CLIENT.name(), transactionId);

        if (rolModel == null) {
            LOGGER.error("[UserService] [create] An error occurred while assigning a role. - [transactionId: {}]",
                    transactionId);
            throw new InternalException("An unexpected error has occurred", transactionId);
        }

        final String passwordEncode = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(passwordEncode);

        UserEntity userRegister = new UserEntity();
        userRegister.setAvatarUrl(PICTURE_DEFAULT_PROFILE_WEB);
        userRegister.setEmail(userDto.getEmail());
        userRegister.setCountry(userDto.getCountry());
        userRegister.setFirstName(userDto.getFirstName());
        userRegister.setCellPhone(userDto.getCellPhone());
        userRegister.setLanguage(userDto.getLanguage());
        userRegister.setPassword(userDto.getPassword());
        userRegister.setLastName(userDto.getLastName());
        userRegister.setDateOfBirth(userDto.getDateOfBirth());
        userRegister.setStatus(true);
        userRegister.setVerified(false);
        userRegister.setRolId(RolMapper.toEntity(rolModel));

        final UserEntity user = saveUserPort.save(userRegister, transactionId);

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(rolModel),
                passwordEncode,
                email,
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

        return new JwtDto(token, refreshToken, expiration, user.getAvatarUrl(), customUserDetails.getLanguage(),
                true);
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
    public void validateEmailCodeForAccount(Long userId, String code, String transactionId) {

        Optional<UserEntity> userFound = findByUserId(userId, transactionId);

        if (userFound.isEmpty() || !userFound.get().getStatus()) {
            throw new InternalException("It was not possible to validate your code, the " +
                    "account is disabled or does not exist.", transactionId);
        }

        if (userFound.get().getVerified()) {
            return;
        }

        UserEntity userModel = userFound.get();

        final String obtainedCode = securityValidationPort.findByCodeEmailValidation(userModel.getEmail(),
                transactionId);

        if (obtainedCode != null && obtainedCode.equals(code)) {
            userModel.setVerified(true);
            saveUserPort.save(userModel, transactionId);
        } else {
            throw new InternalException("The codes are invalid", transactionId);
        }
    }

    @Override
    public void generateRecoveryCode(String email, String transactionId) {

        Optional<UserEntity> account = findByEmailEntity(email, transactionId);

        if (account.isEmpty() || !account.get().getStatus()) {
            throw new InternalException("The email entered does not exist", transactionId);
        }

        Locale locale = new Locale(account.get().getLanguage());

        final String codeOtp = securityValidationPort.generateOtpRecoverAccount(account.get().getEmail(),
                transactionId);

        final String body = i18nService.tr("recovery-password-body", new Object[]{codeOtp.toUpperCase()}, locale);
        final String subject = i18nService.tr("recovery-password-subject", locale);
        mailPort.sendMail(account.get().getEmail(), subject, body, transactionId);
    }

    @Override
    public void resetPasswordWithRecoveryCode(String email, String code, Locale locale, String transactionId) {

        final String codeObtain = securityValidationPort.findByOtpRecoverPassword(email, transactionId);

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

        final String body = i18nService.tr("message-new-password-body", new Object[]{password}, locale);
        final String subject = i18nService.tr("message-new-password-subject", locale);
        mailPort.sendMail(account.get().getEmail(), subject, body, transactionId);
        user.setPassword(passwordEncoder.encode(password));
        saveUserPort.save(user, transactionId);
        securityValidationPort.resetOtpValidation(email, transactionId);
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
        final String code = securityValidationPort.generateCodeValidationMail(user.getEmail(), transactionId);
        final String body = i18nService.tr("send-code-mail-validation-mail", new Object[]{code.toUpperCase()}, locale);

        mailPort.sendCodeMail(user.getEmail(), body, code, locale, transactionId);
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
            throw new InternalException("The current password entered is invalid", transactionId);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        saveUserPort.save(user, transactionId);
    }

}
