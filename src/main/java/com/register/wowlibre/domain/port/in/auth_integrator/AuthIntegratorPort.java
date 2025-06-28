package com.register.wowlibre.domain.port.in.auth_integrator;

import com.register.wowlibre.domain.dto.client.*;

public interface AuthIntegratorPort {
    AuthClientResponse auth(String host, String username, String password, String transactionId);

    void create(String host, String username, String password, byte[] salt, String apiKey, String emulator,
                Integer expansionId, String gmUsername, String gmPassword, String transactionId);
}
