package com.register.wowlibre.application.services.google;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.port.in.google.*;
import com.register.wowlibre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class GoogleService implements GooglePort {
    private final GoogleClient googleClient;

    public GoogleService(GoogleClient googleClient) {
        this.googleClient = googleClient;
    }

    @Override
    public boolean verifyCaptcha(String apiSecret, String token, String ip, String transactionId) {
        return googleClient.verifyRecaptcha(new VerifyCaptchaRequest(apiSecret, token, ip), transactionId).getSuccess();
    }
}
