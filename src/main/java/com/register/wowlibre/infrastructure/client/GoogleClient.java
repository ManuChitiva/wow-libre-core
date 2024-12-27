package com.register.wowlibre.infrastructure.client;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.exception.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@Component
public class GoogleClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleClient.class);

    private final RestTemplate restTemplate;

    public GoogleClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VerifyCaptchaResponse verifyRecaptcha(VerifyCaptchaRequest request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", request.getSecret());
        params.add("response", request.getResponse());


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/recaptcha/api/siteverify", "https://www" +
                        ".google.com"))
                .toUriString();

        try {
            ResponseEntity<VerifyCaptchaResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    VerifyCaptchaResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[GoogleClient] [verifyRecaptcha] Client/Server Error: {}. The request failed with a client " +
                            "or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[GoogleClient] [verifyRecaptcha] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

}
