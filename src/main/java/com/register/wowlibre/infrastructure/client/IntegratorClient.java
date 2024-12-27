package com.register.wowlibre.infrastructure.client;

import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.shared.*;
import org.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@Component
public class IntegratorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegratorClient.class);

    private final RestTemplate restTemplate;

    public IntegratorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public long createAccountGame(String host, AccountGameCreateDto request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        HttpEntity<AccountGameCreateDto> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GenericResponse<Long>> response = restTemplate.exchange(String.format("%s/api/account"
                    + "/create", host), HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
            });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [createAccountGame] Client/Server Error: {}. The request failed with a " +
                            "client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [createAccountGame] Unexpected Error: {}. An unexpected error occurred " +
                            "during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }

    public CharactersResponse characters(String host, String jwt, Long accountId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/characters", host))
                .queryParam(PARAM_ACCOUNT_ID, accountId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<CharactersResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [characters] Client/Server Error: {}. The request failed with a client " +
                            "or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [characters] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public AccountDetailResponse account(String host, String jwt, Long accountId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/account/%s", host, accountId))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<AccountDetailResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public MailsResponse mails(String host, String jwt, Long characterId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/mails/%s", host, characterId))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<MailsResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public void deleteFriend(String host, String jwt, Long characterId, Long friendId, Long accountId, Long userId,
                             String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/social/%s/%s", host, characterId, friendId))
                .queryParam("account_id", accountId)
                .queryParam("user_id", userId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.DELETE,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public CharacterSocialResponse friends(String host, String jwt, Long characterId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/social/%s/friends", host, characterId))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<CharacterSocialResponse>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public void changePasswordGame(String host, String jwt, Long accountId, Long userId, String password, byte[] salt,
                                   String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(new ChangePasswordRequest(password, accountId,
                userId,
                salt), headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/account/change-password", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<CharacterSocialResponse>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public List<CharacterProfessionsResponse> professions(String host, String jwt, Long accountId, Long characterId,
                                                          String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/professions", host))
                .queryParam(PARAM_ACCOUNT_ID, accountId)
                .queryParam("character_id", characterId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<List<CharacterProfessionsResponse>>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<Void> sendLevel(String host, String jwt, SendLevelRequest sendLevelRequest,
                                           String transactionId) {

        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<SendLevelRequest> entity = new HttpEntity<>(sendLevelRequest, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/social/send/level", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendLevel] Client/Server Error: {}. The request failed with a client or" +
                            " server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());

            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendLevel] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("[IntegratorClient] [sendLevel] Unexpected transaction failure", transactionId);

    }

    public GenericResponse<Void> sendMoney(String host, String jwt, SendMoneyRequest sendMoneyRequest,
                                           String transactionId) {

        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<SendMoneyRequest> entity = new HttpEntity<>(sendMoneyRequest, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/social/send/money", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendMoney] Client/Server Error: {}. The request failed with a client or" +
                            " server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendMoney] Unexpected Error: {}. An unexpected error occurred during " +
                            "the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("[IntegratorClient] [sendMoney] Unexpected transaction failure", transactionId);

    }

    public CharactersResponse loanApplicationCharacters(String host, String jwt, Long accountId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/characters/loan/bank", host))
                .queryParam(PARAM_ACCOUNT_ID, accountId)
                .queryParam("time", 1) //24horas
                .queryParam("level", 80)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<CharactersResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Client/Server Error: {}. The request failed with a client or server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("Unexpected Error: {}. An unexpected error occurred during the transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GuildsResponse guilds(String host, String jwt, int size, int page, String search, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds", host))
                .queryParam("size", size)
                .queryParam("page", page)
                .queryParam("search", search)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<GuildsResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [guilds] Client/Server Error: {}. Error with server client getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [guilds] Unexpected Error: {}. An unexpected error occurred during the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public GuildResponse guild(String host, String jwt, Long guid, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds/%s", host, guid))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<GuildResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [guild] Client/Server Error: {}. Error with server client getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [guild] Unexpected Error: {}. An unexpected error occurred during the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public void attachGuild(String host, String jwt, Long guildId, Long accountId,
                            Long characterId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds/attach", host))
                .queryParam("account_id", accountId)
                .queryParam("guild_id", guildId)
                .queryParam("character_id", characterId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<GuildResponse>> response = restTemplate.exchange(url, HttpMethod.PUT,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [attachGuild] Client/Server Error: {}. Error with server client getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [attachGuild] Unexpected Error: {}. An unexpected error occurred during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public GuildDetailMemberResponse guildMember(String host, String jwt, Long userId, Long accountId, Long characterId,
                                                 String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds/member", host))
                .queryParam("account_id", accountId)
                .queryParam("character_id", characterId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<GuildDetailMemberResponse>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [attachGuild] Client/Server Error: {}. Error with server client getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [guildMember] Unexpected Error: {}. An unexpected error occurred during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public void unInviteGuild(String host, String jwt, Long userId, Long accountId,
                              Long characterId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds/member", host))
                .queryParam("user_id", userId)
                .queryParam("account_id", accountId)
                .queryParam("character_id", characterId)
                .toUriString();

        try {
            ResponseEntity<GenericResponse<GuildResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [unInviteGuild] Client/Server Error: {}. Error with server client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [unInviteGuild] Unexpected Error: {}. An unexpected error occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public GenericResponse<Void> sendCommand(String host, String jwt, String message, byte[] salt,
                                             String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        ExecuteCommandRequest request = new ExecuteCommandRequest(message, salt);
        HttpEntity<ExecuteCommandRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/commands", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendCommand]  Client/Server Error: {}. Error with server client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendCommand] Unexpected Error: {}. An unexpected error occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public Double collectGold(String host, String jwt, Long userId, Double moneyToPay,
                              String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        BankCollectGoldRequest request = new BankCollectGoldRequest(userId, moneyToPay);
        HttpEntity<BankCollectGoldRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/bank/payment", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Double>> response = restTemplate.exchange(url, HttpMethod.PUT,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody()).getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [collectGold]  Client/Server Error: {}. Error with server client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            return moneyToPay;
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [collectGold] Unexpected Error: {}. An unexpected error occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            return moneyToPay;
        }

        return moneyToPay;
    }


    public GenericResponse<Void> purchase(String host, String jwt, Long userId, Long accountId, String reference,
                                          List<ItemQuantityModel> items, Double amount, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CreateTransactionItemsRequest request = new CreateTransactionItemsRequest(userId, accountId, reference, items,
                amount);
        HttpEntity<CreateTransactionItemsRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/purchase", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [createTransactionItems]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [createTransactionItems] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

    public GenericResponse<Void> updateGuild(String host, String jwt, Long characterId, Long accountId,
                                             boolean isPublic,
                                             boolean multiFaction, String discord, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        UpdateGuildRequest request = new UpdateGuildRequest(discord, isPublic, multiFaction, accountId, characterId);
        HttpEntity<UpdateGuildRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/guilds/edit", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.PUT,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [updateGuild]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [updateGuild] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<Void> sendAnnouncement(String host, String jwt, Long userId, Long accountId,
                                                  Long characterId,
                                                  Long skillId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        AnnouncementRequest request = new AnnouncementRequest(accountId, characterId, skillId, userId);
        HttpEntity<AnnouncementRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/professions/announcement", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendAnnouncement]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendAnnouncement] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<Void> sendBenefitsPremium(String host, String jwt, SubscriptionBenefitsRequest request,
                                                     String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<SubscriptionBenefitsRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/subscription-benefits", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendBenefitsPremium] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<Void> sendPromo(String host, String jwt, ClaimPromoRequest request,
                                           String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<ClaimPromoRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/claim-promotions", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendPromo]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendPromo] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<Void> sendGuildBenefits(String host, String jwt, BenefitsGuildRequest request,
                                                   String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<BenefitsGuildRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/claim-guild-benefits", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<Void>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [sendGuildBenefits]  Client/Server Error: {}. Error with server " +
                            "client " +
                            "getting " +
                            "associated guilds. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [sendGuildBenefits] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<ClaimMachineResponse> claimMachine(String host, String jwt, ClaimMachineRequest request,
                                                              String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<ClaimMachineRequest> entity = new HttpEntity<>(request, headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/transaction/claim-machine", host))
                .toUriString();

        try {
            ResponseEntity<GenericResponse<ClaimMachineResponse>> response = restTemplate.exchange(url, HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [claimMachine]  Client/Server Error: {}. It was not possible to claim " +
                            "the shipment of the prize to the client because he won at the slot machine " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            return new GenericResponseBuilder<>(new ClaimMachineResponse(false), transactionId).build();
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [claimMachine] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            return new GenericResponseBuilder<>(new ClaimMachineResponse(false), transactionId).build();
        }

        return new GenericResponseBuilder<>(new ClaimMachineResponse(false), transactionId).build();
    }


    public GenericResponse<AccountsResponse> accountsServer(String host, String jwt, int size, int page,
                                                            String filter,
                                                            String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/account/all", host))
                .queryParam("size", size)
                .queryParam("page", page)
                .queryParam("filter", filter)
                .toUriString();


        try {
            ResponseEntity<GenericResponse<AccountsResponse>> response = restTemplate.exchange(url, HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [accountsServer]  Client/Server Error: {}. Could not get server accounts" +
                            "  " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [accountsServer] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }


    public GenericResponse<DashboardMetricsResponse> metricsDashboard(String host, String jwt, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);


        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(String.format("%s/api/dashboard/stats", host))
                .toUriString();


        try {
            ResponseEntity<GenericResponse<DashboardMetricsResponse>> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity, new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().is2xxSuccessful()) {
                return Objects.requireNonNull(response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[IntegratorClient] [accountsServer]  Client/Server Error: {}. Could not get server accounts" +
                            "  " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[IntegratorClient] [accountsServer] Unexpected Error: {}. An unexpected error " +
                            "occurred " +
                            "during " +
                            "the " +
                            "transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Transaction failed due to client or server error", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);
    }

}
