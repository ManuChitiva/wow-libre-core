package com.register.wowlibre.service;

import com.register.wowlibre.application.services.account_game.*;
import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.account_game.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.model.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountGameServiceTest extends BaseTest {

    @Mock
    private SaveAccountGamePort saveAccountGamePort;
    @Mock
    private ObtainAccountGamePort obtainAccountGamePort;
    @Mock
    private RealmPort realmPort;
    @Mock
    private UserPort userPort;
    @Mock
    private IntegratorPort integratorPort;

    private AccountGameService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AccountGameService(saveAccountGamePort, obtainAccountGamePort, realmPort, userPort,
                integratorPort);
    }

    @Test
    void testAccounts_returnsAccountListSuccessfully() {
        Long userId = 1L;
        Long realmId = 2L;
        String transactionId = "tx-004";


        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(createSampleUserEntity()));

        RealmEntity realmEntity = new RealmEntity();
        realmEntity.setStatus(true);
        realmEntity.setId(realmId);
        realmEntity.setExpansionId(2);


        AccountGameEntity accountEntity = new AccountGameEntity();
        accountEntity.setAccountId(123L);
        accountEntity.setUsername("testaccount");
        accountEntity.setStatus(true);
        accountEntity.setRealmId(realmEntity);
        accountEntity.setUserId(createSampleUserEntity());


        when(obtainAccountGamePort.findByUserIdAndRealmId(userId, realmId, transactionId))
                .thenReturn(List.of(accountEntity));

        AccountsGameDto result = service.accounts(userId, realmId, transactionId);

        assertNotNull(result);
        assertEquals(1, result.getAccounts().size());
        assertEquals("testaccount", result.getAccounts().get(0).username());
    }


    @Test
    void testAccounts_userNotFound_throwsException() {
        Long userId = 1L;
        Long realmId = 10L;
        String transactionId = "tx-003";

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                service.accounts(userId, realmId, transactionId)
        );
    }

    @Test
    void testCreateAccount_userReachedMaxAccounts() {
        Long userId = 1L;
        String serverName = "Azeroth";
        Integer expansionId = 2;
        String username = "testuser";
        String password = "123456";
        String transactionId = "tx-002";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@mail.com");

        RealmModel realmModel = RealmModel.builder()
                .id(100L)
                .name(serverName)
                .ip("localhost")
                .apiSecret("secret")
                .expansion(2)
                .build();

        // Simula 20 cuentas ya creadas
        List<AccountGameEntity> existingAccounts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            existingAccounts.add(new AccountGameEntity());
        }

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(userEntity));
        when(realmPort.findByNameAndVersionAndStatusIsTrue(serverName, expansionId, transactionId)).thenReturn(realmModel);
        when(obtainAccountGamePort.findByUserIdAndRealmId(userId, realmModel.id, transactionId)).thenReturn(existingAccounts);

        assertThrows(InternalException.class, () ->
                service.create(userId, serverName, expansionId, username, password, transactionId)
        );
    }


    @Test
    void testCreateAccount_server() {
        Long userId = 1L;
        String serverName = "Azeroth";
        Integer expansionId = 2;
        String username = "testuser";
        String password = "123456";
        String transactionId = "tx-001";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@mail.com");


        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(userEntity));
        when(realmPort.findByNameAndVersionAndStatusIsTrue(serverName, expansionId, transactionId)).thenReturn(null);
        when(integratorPort.createAccount(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(999L);

        assertThrows(InternalException.class, () ->
                service.create(userId, serverName, expansionId, username, password, transactionId)
        );
    }


    @Test
    void testCreateAccount_serverNotFound() {
        Long userId = 1L;
        String serverName = "Azeroth";
        Integer expansionId = 2;
        String username = "testuser";
        String password = "123456";
        String transactionId = "tx-001";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@mail.com");
        RealmModel realmModel = RealmModel.builder().ip("localhost").build();

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(userEntity));
        when(realmPort.findByNameAndVersionAndStatusIsTrue(serverName, expansionId, transactionId)).thenReturn(null);
        when(obtainAccountGamePort.findByUserIdAndRealmId(userId, realmModel.id, transactionId)).thenReturn(Collections.emptyList());
        when(integratorPort.createAccount(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(999L);

        assertThrows(InternalException.class, () ->
                service.create(userId, serverName, expansionId, username, password, transactionId)
        );
    }


    @Test
    void testCreateAccount_success() {
        Long userId = 1L;
        String serverName = "Azeroth";
        Integer expansionId = 2;
        String username = "testuser";
        String password = "123456";
        String transactionId = "tx-001";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@mail.com");

        RealmModel realmModel = RealmModel.builder().ip("localhost").build();

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(userEntity));
        when(realmPort.findByNameAndVersionAndStatusIsTrue(serverName, expansionId, transactionId)).thenReturn(realmModel);
        when(obtainAccountGamePort.findByUserIdAndRealmId(userId, realmModel.id, transactionId)).thenReturn(Collections.emptyList());
        when(integratorPort.createAccount(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(999L);

        assertDoesNotThrow(() -> service.create(userId, serverName, expansionId, username, password, transactionId));
        verify(saveAccountGamePort).save(any(AccountGameEntity.class), eq(transactionId));
    }

    @Test
    void testCreateAccount_userNotFound() {
        when(userPort.findByUserId(1L, "tx")).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                service.create(1L, "Realm", 1, "user", "pass", "tx")
        );

        assertEquals("The client is not available or does not exist", ex.getMessage());
    }

    @Test
    void testAccounts_byPage_success() {
        Long userId = 1L;
        String transactionId = "tx";
        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(new UserEntity()));
        when(obtainAccountGamePort.accounts(userId)).thenReturn(1L);
        when(obtainAccountGamePort.findByUserIdAndStatusIsTrue(userId, 0, 10, transactionId))
                .thenReturn(List.of(buildAccountGameEntity()));

        AccountsGameDto result = service.accounts(userId, 0, 10, null, null, transactionId);

        assertNotNull(result);
        assertEquals(1, result.getAccounts().size());
    }

    @Test
    void testVerifyAccount_success() {
        Long userId = 1L, realmId = 1L, accountId = 101L;
        String transactionId = "tx";

        RealmEntity realm = new RealmEntity();
        realm.setId(realmId);
        realm.setStatus(true);

        AccountGameEntity account = buildAccountGameEntity();

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.of(realm));
        when(obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId, realmId,
                transactionId))
                .thenReturn(Optional.of(account));

        AccountVerificationDto result = service.verifyAccount(userId, accountId, realmId, transactionId);

        assertNotNull(result);
        assertEquals(account.getId(), result.accountGame().getId());
    }

    private AccountGameEntity buildAccountGameEntity() {
        AccountGameEntity entity = new AccountGameEntity();
        entity.setId(101L);
        entity.setAccountId(200L);
        entity.setUsername("gameuser");
        entity.setStatus(true);

        UserEntity user = new UserEntity();
        user.setEmail("email@example.com");
        entity.setUserId(user);

        RealmEntity realm = new RealmEntity();
        realm.setId(1L);
        realm.setName("Azeroth");
        realm.setStatus(true);
        realm.setExpansionId(Expansion.WRATH_OF_THE_LICH_KING.getValue());

        entity.setRealmId(realm);
        return entity;
    }

    @Test
    void testAccount_realmNotFoundOrInactive_throwsException() {
        Long userId = 1L, accountId = 100L, realmId = 10L;
        String transactionId = "tx-001";

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                service.account(userId, accountId, realmId, transactionId)
        );
    }

    @Test
    void testAccount_realmInactive_throwsException() {
        Long userId = 1L, accountId = 100L, realmId = 10L;
        String transactionId = "tx-002";

        RealmEntity realm = new RealmEntity();
        realm.setStatus(false);

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.of(realm));

        assertThrows(InternalException.class, () ->
                service.account(userId, accountId, realmId, transactionId)
        );
    }

    @Test
    void testAccount_accountNotFound_throwsException() {
        Long userId = 1L, accountId = 100L, realmId = 10L;
        String transactionId = "tx-003";

        RealmEntity realm = new RealmEntity();
        realm.setId(realmId);
        realm.setStatus(true);
        realm.setHost("localhost");
        realm.setJwt("jwt-token");
        realm.setName("Azeroth");

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.of(realm));
        when(obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId, realmId,
                transactionId))
                .thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                service.account(userId, accountId, realmId, transactionId)
        );
    }

    @Test
    void testAccount_success_returnsAccountGameDetailDto() {
        Long userId = 1L, accountId = 100L, realmId = 10L;
        String transactionId = "tx-004";

        RealmEntity realm = new RealmEntity();
        realm.setId(realmId);
        realm.setStatus(true);
        realm.setHost("localhost");
        realm.setJwt("jwt-token");
        realm.setName("Azeroth");

        AccountGameEntity accountGameEntity = new AccountGameEntity();

        AccountDetailResponse accountResponse = new AccountDetailResponse(
                accountId, "testuser", "test@mail.com", "exp1", true, "192.168.1.1",
                LocalDate.now(), "127.0.0.1", "admin", "spam",
                false, LocalDate.now(), "os", null
        );

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.of(realm));
        when(obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId, realmId,
                transactionId))
                .thenReturn(Optional.of(accountGameEntity));
        when(integratorPort.account(realm.getHost(), realm.getJwt(), accountId, transactionId))
                .thenReturn(accountResponse);

        AccountGameDetailDto result = service.account(userId, accountId, realmId, transactionId);

        assertNotNull(result);
        assertEquals("test@mail.com", result.email());
        assertEquals("exp1", result.expansion());
        assertEquals(accountId, result.id());
    }

    @Test
    void testVerifyAccount_realmIsEmpty() {
        Long userId = 1L, realmId = 1L, accountId = 101L;
        String transactionId = "tx";

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                service.verifyAccount(userId, accountId, realmId, transactionId)
        );
    }

    @Test
    void testVerifyAccount_accountGameIsEmpty_shouldThrowException() {
        Long userId = 1L, accountId = 101L, realmId = 1L;
        String transactionId = "tx";

        RealmEntity realm = new RealmEntity();
        realm.setId(realmId);

        when(realmPort.findById(realmId, transactionId)).thenReturn(Optional.of(realm));
        when(obtainAccountGamePort.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(userId, accountId, realmId,
                transactionId))
                .thenReturn(Optional.empty());

        assertThrows(InternalException.class, () ->
                service.verifyAccount(userId, accountId, realmId, transactionId)
        );
    }

    @Test
    void testAccounts_sizeGreaterThan30_shouldLimitTo30() {
        Long userId = 1L;
        int page = 0;
        int size = 50; // mayor a 30
        String searchUsername = null;
        String realmName = null;
        String transactionId = "tx-001";

        UserEntity user = new UserEntity();
        user.setId(userId);

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(user));
        when(obtainAccountGamePort.accounts(userId)).thenReturn(0L);

        AccountsGameDto result = service.accounts(userId, page, size, searchUsername, realmName, transactionId);

        assertNotNull(result);
        assertEquals(0, result.getAccounts().size());
        assertEquals(0, result.getSize());
    }

    @Test
    void testAccounts_userNotFound_shouldThrowUnauthorizedException() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String searchUsername = null;
        String realmName = null;
        String transactionId = "tx-002";

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () ->
                service.accounts(userId, page, size, searchUsername, realmName, transactionId)
        );
    }

    @Test
    void testAccounts_withSearchFilters_shouldCallFilteredQuery() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String searchUsername = "testuser";
        String realmName = "Azeroth";
        String transactionId = "tx-003";

        UserEntity user = new UserEntity();
        user.setId(userId);



        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(user));
        when(obtainAccountGamePort.accounts(userId)).thenReturn(1L);
        when(obtainAccountGamePort.findByUserIdAndRealmNameAndUsernameStatusIsTrue(userId, page, size, realmName,
                searchUsername, transactionId))
                .thenReturn(List.of(buildAccountGameEntity() ));

        when(userPort.findByUserId(userId, transactionId)).thenReturn(Optional.of(user));
        when(obtainAccountGamePort.accounts(userId)).thenReturn(1L);
        when(obtainAccountGamePort.findByUserIdAndRealmNameAndUsernameStatusIsTrue(userId, page, size, realmName,
                searchUsername, transactionId))
                .thenReturn(List.of(buildAccountGameEntity()));

        // Aquí puedes usar un `spy` para verificar si se llama mapToModel correctamente o simular directamente su
        // salida

        AccountsGameDto result = service.accounts(userId, page, size, searchUsername, realmName, transactionId);

        assertNotNull(result);
        assertEquals(1, result.getAccounts().size());
        assertEquals(1L, result.getSize());
    }

}
