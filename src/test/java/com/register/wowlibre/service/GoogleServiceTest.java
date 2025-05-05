package com.register.wowlibre.service;

import com.register.wowlibre.application.services.google.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.infrastructure.client.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoogleServiceTest {
    @Mock
    private GoogleClient googleClient;

    private GoogleService googleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        googleService = new GoogleService(googleClient);
    }

    @Test
    void verifyCaptcha_shouldReturnTrue_whenCaptchaIsValid() {
        String apiSecret = "secret";
        String token = "valid-token";
        String ip = "127.0.0.1";
        String txId = "tx123";

        VerifyCaptchaResponse response = new VerifyCaptchaResponse();
        response.setSuccess(true);

        when(googleClient.verifyRecaptcha(any(VerifyCaptchaRequest.class), eq(txId))).thenReturn(response);

        boolean result = googleService.verifyCaptcha(apiSecret, token, ip, txId);

        assertTrue(result);
        verify(googleClient).verifyRecaptcha(any(VerifyCaptchaRequest.class), eq(txId));
    }

    @Test
    void verifyCaptcha_shouldReturnFalse_whenCaptchaIsInvalid() {
        String apiSecret = "secret";
        String token = "invalid-token";
        String ip = "127.0.0.1";
        String txId = "tx123";

        VerifyCaptchaResponse response = new VerifyCaptchaResponse();
        response.setSuccess(false);

        when(googleClient.verifyRecaptcha(any(VerifyCaptchaRequest.class), eq(txId))).thenReturn(response);

        boolean result = googleService.verifyCaptcha(apiSecret, token, ip, txId);

        assertFalse(result);
        verify(googleClient).verifyRecaptcha(any(VerifyCaptchaRequest.class), eq(txId));
    }
}
