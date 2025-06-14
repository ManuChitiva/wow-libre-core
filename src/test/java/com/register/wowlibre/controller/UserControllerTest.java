package com.register.wowlibre.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.security.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.controller.*;
import com.register.wowlibre.model.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest extends BaseTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserPort userPort;

    @Mock
    private HttpServletRequest httpServletRequest;

    private static final String TRANSACTION_ID = "12345";
    private static final Locale LOCALE = Locale.US;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void detail_shouldReturnUserDetails() {
        Long userId = 1L;
        var mockUser = createSampleUserEntity();
        when(userPort.findByUserId(userId, TRANSACTION_ID)).thenReturn(Optional.of(mockUser));
        ResponseEntity<GenericResponse<UserDetailDto>> response =
                userController.detail(TRANSACTION_ID, userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John", Objects.requireNonNull(response.getBody()).getData().getFirstName());
    }

    @Test
    void detail_shouldReturnNull() {
        Long userId = 1L;
        when(userPort.findByUserId(userId, TRANSACTION_ID)).thenReturn(Optional.empty());
        ResponseEntity<GenericResponse<UserDetailDto>> response =
                userController.detail(TRANSACTION_ID, userId);
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void create_shouldReturnCreatedResponse() {
        UserDto userDto = new UserDto();
        JwtDto jwtDto = new JwtDto(1L, "token", "", new Date(), "", "ES", false);

        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(userPort.create(eq(userDto), anyString(), eq(LOCALE), eq(TRANSACTION_ID)))
                .thenReturn(jwtDto);

        ResponseEntity<GenericResponse<JwtDto>> response = userController.create(
                TRANSACTION_ID, LOCALE, userDto, httpServletRequest);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(Objects.requireNonNull(response.getBody()).getData());
        assertEquals("token", response.getBody().getData().jwt);
    }

    @Test
    void search_withEmail_shouldReturnExistenceTrue() {

        String email = "test@example.com";
        UserModel userModel = UserModel.builder().build();
        userModel.id = 1L;
        userModel.email = email;

        when(userPort.findByEmail(email, TRANSACTION_ID)).thenReturn(userModel);

        ResponseEntity<GenericResponse<UserExistenceDto>> response =
                userController.search(TRANSACTION_ID, email, null);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).getData().exist());
    }

    @Test
    void search_withPhone_shouldReturnExistenceTrue() {

        String phone = "3214424552";
        UserModel userModel = UserModel.builder().build();
        userModel.id = 1L;
        userModel.cellPhone = phone;

        when(userPort.findByPhone(phone, TRANSACTION_ID)).thenReturn(userModel);

        ResponseEntity<GenericResponse<UserExistenceDto>> response =
                userController.search(TRANSACTION_ID, null, phone);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).getData().exist());
    }

    @Test
    void search_withoutParams_shouldReturnBadRequest() {
        ResponseEntity<GenericResponse<UserExistenceDto>> response =
                userController.search(TRANSACTION_ID, null, null);
        assertEquals(400, response.getStatusCode().value());
        assertNull(Objects.requireNonNull(response.getBody()).getData());
    }


    @Test
    void validateEmailCodeForAccount_shouldCallPort() {
        Long userId = 1L;
        String code = "abc123";

        ResponseEntity<GenericResponse<Void>> response =
                userController.validateEmailCodeForAccount(TRANSACTION_ID, userId, code);

        verify(userPort).validateEmailCodeForAccount(userId, code, TRANSACTION_ID);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void requestPasswordRecoveryCode_shouldCallPort() {
        String email = "test@example.com";

        ResponseEntity<GenericResponse<Void>> response =
                userController.requestPasswordRecoveryCode(TRANSACTION_ID, email);

        verify(userPort).generateRecoveryCode(email, TRANSACTION_ID);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void validateRecoveryCodeAndResetPassword_shouldCallPort() {
        String email = "test@example.com";
        String code = "1234";

        ResponseEntity<GenericResponse<Void>> response =
                userController.validateRecoveryCodeAndResetPassword(TRANSACTION_ID, LOCALE, email, code);

        verify(userPort).resetPasswordWithRecoveryCode(email, code, LOCALE, TRANSACTION_ID);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void sendValidationEmail_shouldCallPort() {
        String email = "test@example.com";

        ResponseEntity<GenericResponse<Void>> response =
                userController.sendValidationEmail(TRANSACTION_ID, email);

        verify(userPort).sendMailValidation(email, TRANSACTION_ID);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void newPassword_shouldCallPort() {
        Long userId = 1L;
        ChangePasswordUserDto dto = new ChangePasswordUserDto();
        dto.setPassword("oldPass");
        dto.setNewPassword("newPass");

        ResponseEntity<GenericResponse<Void>> response =
                userController.newPassword(TRANSACTION_ID, userId, dto);

        verify(userPort).changePassword(userId, "oldPass", "newPass", TRANSACTION_ID);
        assertEquals(200, response.getStatusCode().value());
    }
}
