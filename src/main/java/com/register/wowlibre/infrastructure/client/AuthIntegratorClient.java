package com.register.wowlibre.infrastructure.client;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.shared.*;
import org.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@Component
public class AuthIntegratorClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegratorClient.class);

    private final RestTemplate restTemplate;

    public AuthIntegratorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthClientResponse auth(String host, AuthClientRequest request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        HttpEntity<AuthClientRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GenericResponse<AuthClientResponse>> response = restTemplate.exchange(String.format("%s" +
                    "/api/auth/login", host), HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
            });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[AuthIntegratorClient] [auth] Client/Server Error: {}. The request failed with a " +
                            "client or realm error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or realm error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[AuthIntegratorClient] [auth] Unexpected Error: {}. An unexpected error occurred " +
                            "during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }


    public Void create(String host, AuthClientCreateRequest request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        HttpEntity<AuthClientCreateRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(String.format("%s" +
                            "/api/client",
                    host), HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
            });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[AuthIntegratorClient] [create] Client/Server Error: {}. The request failed with a " +
                            "client or realm error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or realm error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[AuthIntegratorClient] [create] Unexpected Error: {}. An unexpected error occurred " +
                            "during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }

}
