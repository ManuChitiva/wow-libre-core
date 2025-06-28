package com.register.wowlibre.application.services.auth;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.port.in.auth_integrator.*;
import com.register.wowlibre.infrastructure.client.*;
import org.springframework.stereotype.*;

@Service
public class AuthIntegratorService implements AuthIntegratorPort {

    private final AuthIntegratorClient authIntegratorClient;

    public AuthIntegratorService(AuthIntegratorClient authIntegratorClient) {
        this.authIntegratorClient = authIntegratorClient;
    }

    @Override
    public AuthClientResponse auth(String host, String username, String password, String transactionId) {
        return authIntegratorClient.auth(host, new AuthClientRequest(username, password), transactionId);
    }

    @Override
    public void create(String host, String username, String password, byte[] salt, String apiKey, String emulator,
                       Integer expansionId, String gmUsername, String gmPassword, String transactionId) {
        authIntegratorClient.create(host, new AuthClientCreateRequest(username, password, salt, apiKey, emulator,
                expansionId, gmUsername, gmPassword), transactionId);
    }
}
