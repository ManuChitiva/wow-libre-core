package com.register.wowlibre.service;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.application.services.user.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.google.*;
import com.register.wowlibre.domain.port.in.jwt.*;
import com.register.wowlibre.domain.port.in.mail.*;
import com.register.wowlibre.domain.port.in.rol.*;
import com.register.wowlibre.domain.port.in.security_validation.*;
import com.register.wowlibre.domain.port.out.user.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.infrastructure.config.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import com.register.wowlibre.model.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends BaseTest {

    @Mock
    private ObtainUserPort obtainUserPort;
    @Mock
    private SaveUserPort saveUserPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtPort jwtPort;
    @Mock
    private RolPort rolPort;
    @Mock
    private MailPort mailPort;
    @Mock
    private SecurityValidationPort securityValidationPort;
    @Mock
    private I18nService i18nService;
    @Mock
    private RandomString randomString;
    @Mock
    private GooglePort googlePort;
    @Mock
    private Configurations configurations;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                obtainUserPort,
                saveUserPort,
                passwordEncoder,
                jwtPort,
                rolPort,
                mailPort,
                securityValidationPort,
                i18nService,
                randomString,
                googlePort,
                configurations
        );
    }

    @Test
    void shouldThrowExceptionWhenCodeIsInvalid() {
        Long userId = 5L;
        String inputCode = "WRONGCODE";
        String transactionId = "tx005";

        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setStatus(true);
        user.setVerified(false);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));
        when(securityValidationPort.findByCodeEmailValidation(user.getEmail(), transactionId)).thenReturn("VALIDCODE");

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.validateEmailCodeForAccount(userId, inputCode, transactionId)
        );

        assertEquals("The codes are invalid", ex.getMessage());
    }


    @Test
    void shouldDoNothingWhenUserIsAlreadyVerified() {
        Long userId = 4L;
        String code = "ANY";
        String transactionId = "tx004";

        UserEntity user = new UserEntity();
        user.setStatus(true);
        user.setVerified(true);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));

        userService.validateEmailCodeForAccount(userId, code, transactionId);

        verifyNoInteractions(saveUserPort);
        verify(securityValidationPort, never()).findByCodeEmailValidation(any(), any());
    }


    @Test
    void shouldValidateEmailCodeSuccessfully() {
        Long userId = 1L;
        String code = "ABC123";
        String transactionId = "tx001";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setStatus(true);
        user.setVerified(false);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));
        when(securityValidationPort.findByCodeEmailValidation(user.getEmail(), transactionId)).thenReturn(code);

        userService.validateEmailCodeForAccount(userId, code, transactionId);

        assertTrue(user.getVerified());
        verify(saveUserPort).save(user, transactionId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOrInactive() {
        Long userId = 2L;
        String code = "XYZ";
        String transactionId = "tx002";

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.empty());

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.validateEmailCodeForAccount(userId, code, transactionId)
        );

        assertTrue(ex.getMessage().contains("account is disabled or does not exist"));
    }

    @Test
    void ValidateEmailCodeForAccountShouldThrowExceptionWhenUserIsInactive() {
        Long userId = 3L;
        String code = "XYZ";
        String transactionId = "tx003";

        UserEntity user = new UserEntity();
        user.setStatus(false);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.validateEmailCodeForAccount(userId, code, transactionId)
        );

        assertTrue(ex.getMessage().contains("account is disabled or does not exist"));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String email = "missing@example.com";
        String inputCode = "CODE123";
        String transactionId = "tx004";
        Locale locale = new Locale("es");

        when(securityValidationPort.findByOtpRecoverPassword(email, transactionId)).thenReturn("CODE123");
        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.empty());

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.resetPasswordWithRecoveryCode(email, inputCode, locale, transactionId)
        );

        assertEquals("It was not possible to assign a new password, please contact support", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOtpCodeIsInvalid() {
        String email = "user@example.com";
        String inputCode = "WRONGCODE";
        String otpFromSystem = "correctcode";
        String transactionId = "tx003";
        Locale locale = new Locale("es");

        when(securityValidationPort.findByOtpRecoverPassword(email, transactionId)).thenReturn(otpFromSystem);

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.resetPasswordWithRecoveryCode(email, inputCode, locale, transactionId)
        );

        assertEquals("The security code is invalid", ex.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenOtpCodeIsNull() {
        String email = "user@example.com";
        String inputCode = "ANYCODE";
        String transactionId = "tx002";
        Locale locale = new Locale("es");

        when(securityValidationPort.findByOtpRecoverPassword(email, transactionId)).thenReturn(null);

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.resetPasswordWithRecoveryCode(email, inputCode, locale, transactionId)
        );

        assertEquals("Expired security code.", ex.getMessage());
    }


    @Test
    void shouldResetPasswordSuccessfully() {
        String email = "user@example.com";
        String inputCode = "ABC123";
        String otpFromSystem = "abc123";
        String transactionId = "tx001";
        Locale locale = new Locale("es");
        String newPassword = "newPass123";
        String encodedPassword = "encodedPass";
        String subject = "Asunto";
        String body = "Cuerpo del mensaje";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setStatus(true);

        // Mocks
        when(securityValidationPort.findByOtpRecoverPassword(email, transactionId)).thenReturn(otpFromSystem);
        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));
        when(randomString.nextString()).thenReturn(newPassword);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(i18nService.tr(eq("message-new-password-body"), any(Object[].class), eq(locale))).thenReturn(body);
        when(i18nService.tr(eq("message-new-password-subject"), eq(locale))).thenReturn(subject);

        // Ejecutar método
        userService.resetPasswordWithRecoveryCode(email, inputCode, locale, transactionId);

        // Verificaciones
        verify(mailPort).sendMail(email, subject, body, transactionId);
        verify(saveUserPort).save(user, transactionId);
        verify(securityValidationPort).resetOtpValidation(email, transactionId);
        assertEquals(encodedPassword, user.getPassword());
    }

    @Test
    void shouldGenerateRecoveryCodeSuccessfully() {
        String email = "test@example.com";
        String transactionId = "1234";
        String otpCode = "abc123";
        String language = "es";
        String subject = "Recuperación de contraseña";
        String body = "Su código de recuperación es: ABC123";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setLanguage(language);
        user.setStatus(true); // Usuario activo

        // Mock comportamiento
        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));
        when(securityValidationPort.generateOtpRecoverAccount(email, transactionId)).thenReturn(otpCode);
        when(i18nService.tr(eq("recovery-password-body"), any(Object[].class), any(Locale.class))).thenReturn(body);
        when(i18nService.tr(eq("recovery-password-subject"), any(Locale.class))).thenReturn(subject);

        // Ejecutar método
        userService.generateRecoveryCode(email, transactionId);

        // Verificaciones
        verify(securityValidationPort).generateOtpRecoverAccount(email, transactionId);
        verify(mailPort).sendMail(email, subject, body, transactionId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        String email = "notfound@example.com";
        String transactionId = "tx123";

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                userService.generateRecoveryCode(email, transactionId)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsInactive() {
        String email = "inactive@example.com";
        String transactionId = "tx456";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setLanguage("es");
        user.setStatus(false); // Inactivo

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));

        assertThrows(InternalException.class, () ->
                userService.generateRecoveryCode(email, transactionId)
        );
    }


    @Test
    void create_shouldRegisterUserAndReturnJwtDto_whenValidInput() {
        // Arrange
        String transactionId = "tx123";
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setCountry("USA");
        userDto.setCellPhone("1234567890");
        userDto.setLanguage("en");
        userDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userDto.setToken("validToken");

        RolModel rolModel = new RolModel(1L, Rol.CLIENT.name(), true);


        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setEmail(email);
        savedUser.setStatus(true);
        savedUser.setAvatarUrl("https://default-avatar.com");

        when(configurations.getGoogleSecret()).thenReturn("google-secret");
        when(googlePort.verifyCaptcha("google-secret", "validToken", "127.0.0.1", transactionId)).thenReturn(true);
        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.empty());
        when(rolPort.findByName(Rol.CLIENT.name(), transactionId)).thenReturn(rolModel);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(saveUserPort.save(any(UserEntity.class), eq(transactionId))).thenReturn(savedUser);
        when(jwtPort.generateToken(any())).thenReturn("jwt-token");
        when(jwtPort.extractExpiration(any())).thenReturn(new Date());
        when(jwtPort.generateRefreshToken(any())).thenReturn("refresh-token");

        // Act
        JwtDto result = userService.create(userDto, "127.0.0.1", Locale.ENGLISH, transactionId);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.jwt);
        assertEquals("refresh-token", result.refreshToken);
        assertTrue(result.pendingValidation);

        verify(googlePort).verifyCaptcha(any(), eq("validToken"), eq("127.0.0.1"), eq(transactionId));
        verify(saveUserPort).save(any(UserEntity.class), eq(transactionId));
    }

    @Test
    void create_shouldThrowFoundException_whenEmailExists() {
        // Arrange
        String email = "test@example.com";
        String transactionId = "tx123";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setToken("token");
        UserEntity userFound = new UserEntity();
        userFound.setEmail("emailUserFound@gmail.com");
        userFound.setPassword("password");
        userFound.setFirstName("John");
        userFound.setLastName("Doe");
        userFound.setCountry("USA");
        userFound.setCellPhone("1234567890");
        userFound.setLanguage("en");
        userFound.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userFound.setStatus(true);
        userFound.setVerified(false);

        when(configurations.getGoogleSecret()).thenReturn("google-secret");
        when(googlePort.verifyCaptcha(any(), any(), any(), any())).thenReturn(true);
        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(userFound));

        // Act & Assert
        assertThrows(FoundException.class, () ->
                userService.create(userDto, "127.0.0.1", Locale.ENGLISH, transactionId));
    }

    @Test
    void create_shouldThrowInternalException_whenCaptchaFails() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setToken("invalidToken");
        String transactionId = "txFail";

        when(configurations.getGoogleSecret()).thenReturn("secret");
        when(googlePort.verifyCaptcha(any(), any(), any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(InternalException.class, () ->
                userService.create(userDto, "127.0.0.1", Locale.ENGLISH, transactionId));
    }

    @Test
    void create_shouldThrowInternalException_whenRoleIsNull() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("new@example.com");
        userDto.setToken("token");

        String transactionId = "txRole";

        when(configurations.getGoogleSecret()).thenReturn("google-secret");
        when(googlePort.verifyCaptcha(any(), any(), any(), any())).thenReturn(true);
        when(obtainUserPort.findByEmailAndStatusIsTrue(any())).thenReturn(Optional.empty());
        when(rolPort.findByName(Rol.CLIENT.name(), transactionId)).thenReturn(null);

        // Act & Assert
        assertThrows(InternalException.class, () ->
                userService.create(userDto, "127.0.0.1", Locale.ENGLISH, transactionId));
    }

    @Test
    void findByEmail_shouldReturnUserModel_whenUserExists() {
        String email = "test@example.com";
        String tx = "tx123";
        UserEntity entity = createSampleUserEntity();

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(entity));

        UserModel result = userService.findByEmail(email, tx);

        assertNotNull(result);
        assertEquals(email, result.email);
        verify(obtainUserPort).findByEmailAndStatusIsTrue(email);
    }

    @Test
    void findByEmail_shouldReturnNull_whenUserDoesNotExist() {
        when(obtainUserPort.findByEmailAndStatusIsTrue("notfound@example.com")).thenReturn(Optional.empty());

        UserModel result = userService.findByEmail("notfound@example.com", "tx123");

        assertNull(result);
    }

    @Test
    void findByPhone_shouldReturnUserModel_whenUserExists() {
        String phone = "1234567890";
        String tx = "tx123";
        UserEntity entity = createSampleUserEntity();

        when(obtainUserPort.findByCellPhoneAndStatusIsTrue(phone, tx)).thenReturn(Optional.of(entity));

        UserModel result = userService.findByPhone(phone, tx);

        assertNotNull(result);
        assertEquals(phone, result.cellPhone);
    }

    @Test
    void findByUserId_shouldReturnUserEntity_whenFound() {
        UserEntity entity = createSampleUserEntity();
        when(obtainUserPort.findByUserIdAndStatusIsTrue(1L, "tx123")).thenReturn(Optional.of(entity));

        Optional<UserEntity> result = userService.findByUserId(1L, "tx123");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmailEntity_shouldReturnUserEntity_whenFound() {
        UserEntity entity = createSampleUserEntity();
        when(obtainUserPort.findByEmailAndStatusIsTrue("test@example.com")).thenReturn(Optional.of(entity));

        Optional<UserEntity> result = userService.findByEmailEntity("test@example.com", "tx123");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void shouldSendMailValidationSuccessfully() {
        String email = "user@example.com";
        String transactionId = "tx001";
        String code = "abc123";
        String language = "es";
        Locale locale = new Locale(language);
        String body = "Cuerpo del correo";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setLanguage(language);
        user.setVerified(false);

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));
        when(securityValidationPort.generateCodeValidationMail(email, transactionId)).thenReturn(code);
        when(i18nService.tr(eq("send-code-mail-validation-mail"), any(Object[].class), eq(locale))).thenReturn(body);

        userService.sendMailValidation(email, transactionId);

        verify(mailPort).sendCodeMail(email, body, code, locale, transactionId);
    }


    @Test
    void shouldNotSendMailIfUserAlreadyVerified() {
        String email = "user@example.com";
        String transactionId = "tx001";
        String language = "es";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setLanguage(language);
        user.setVerified(true);

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));

        userService.sendMailValidation(email, transactionId);

        // Verifica que NO se haya enviado el correo
        verify(mailPort, never()).sendCodeMail(anyString(), anyString(), anyString(), any(Locale.class), anyString());

        // Opcionalmente, también puedes verificar que NO se haya generado ningún código
        verify(securityValidationPort, never()).generateCodeValidationMail(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInSendMailValidation() {
        String email = "missing@example.com";
        String transactionId = "tx002";

        when(obtainUserPort.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.empty());

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.sendMailValidation(email, transactionId)
        );

        assertTrue(ex.getMessage().contains("It was not possible to assign a new password"));
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        Long userId = 1L;
        String oldPassword = "old123";
        String newPassword = "new123";
        String encodedPassword = "encodedNewPass";
        String transactionId = "tx004";

        UserEntity user = new UserEntity();
        user.setPassword("hashedOld");
        user.setStatus(true);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userService.changePassword(userId, oldPassword, newPassword, transactionId);

        assertEquals(encodedPassword, user.getPassword());
        verify(saveUserPort).save(user, transactionId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInChangePassword() {
        Long userId = 2L;
        String oldPassword = "old";
        String newPassword = "new";
        String transactionId = "tx005";

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.empty());

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.changePassword(userId, oldPassword, newPassword, transactionId)
        );

        assertTrue(ex.getMessage().contains("It was not possible to change the password"));
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsInvalid() {
        Long userId = 3L;
        String oldPassword = "wrong";
        String newPassword = "newpass";
        String transactionId = "tx006";

        UserEntity user = new UserEntity();
        user.setPassword("hashed");
        user.setStatus(true);

        when(obtainUserPort.findByUserIdAndStatusIsTrue(userId, transactionId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, "hashed")).thenReturn(false);

        InternalException ex = assertThrows(InternalException.class, () ->
                userService.changePassword(userId, oldPassword, newPassword, transactionId)
        );

        assertEquals("The current password entered is invalid", ex.getMessage());
    }


}
