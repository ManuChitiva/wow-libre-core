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


}
