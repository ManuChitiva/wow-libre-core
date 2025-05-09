package com.register.wowlibre.service;

import com.register.wowlibre.application.services.account_game.*;
import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.out.account_game.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountGameServiceTest {
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
}
