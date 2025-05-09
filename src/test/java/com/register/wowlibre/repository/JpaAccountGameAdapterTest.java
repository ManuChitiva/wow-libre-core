package com.register.wowlibre.repository;

import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.repositories.account_game.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaAccountGameAdapterTest {
    @Mock
    private AccountGameRepository accountGameRepository;

    private JpaAccountGameAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new JpaAccountGameAdapter(accountGameRepository);
    }

    @Test
    void findByUserIdAndStatusIsTrue_shouldReturnListFromPage() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<AccountGameEntity> entities = List.of(new AccountGameEntity());
        Page<AccountGameEntity> pageResult = new PageImpl<>(entities);

        when(accountGameRepository.findByUserId_IdAndStatusIsTrue(userId, pageable)).thenReturn(pageResult);

        List<AccountGameEntity> result = adapter.findByUserIdAndStatusIsTrue(userId, page, size, "tx123");

        assertEquals(1, result.size());
        verify(accountGameRepository).findByUserId_IdAndStatusIsTrue(userId, pageable);
    }

    @Test
    void findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue_shouldReturnOptional() {
        Long userId = 1L, accountId = 2L, realmId = 3L;
        Optional<AccountGameEntity> expected = Optional.of(new AccountGameEntity());

        when(accountGameRepository.findByUserId_IdAndAccountIdAndRealmId_idAndStatusIsTrue(userId, accountId, realmId))
                .thenReturn(expected);

        Optional<AccountGameEntity> result = adapter.findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(
                userId, accountId, realmId, "tx123");

        assertTrue(result.isPresent());
        verify(accountGameRepository).findByUserId_IdAndAccountIdAndRealmId_idAndStatusIsTrue(userId, accountId, realmId);
    }

    @Test
    void accounts_shouldReturnCount() {
        Long userId = 1L;
        when(accountGameRepository.countByUserId(userId)).thenReturn(5L);

        Long result = adapter.accounts(userId);

        assertEquals(5L, result);
        verify(accountGameRepository).countByUserId(userId);
    }

    @Test
    void findByUserIdAndRealmNameAndUsernameStatusIsTrue_shouldReturnListFromPage() {
        Long userId = 1L;
        int page = 0, size = 5;
        String realmName = "RealmA";
        String username = "user123";
        Pageable pageable = PageRequest.of(page, size);
        List<AccountGameEntity> entities = List.of(new AccountGameEntity());
        Page<AccountGameEntity> pageResult = new PageImpl<>(entities);

        when(accountGameRepository.findByUserId_IdAndStatusIsTrueAndRealmNameAndUsername(
                realmName, userId, username, pageable)).thenReturn(pageResult);

        List<AccountGameEntity> result = adapter.findByUserIdAndRealmNameAndUsernameStatusIsTrue(
                userId, page, size, realmName, username, "tx123");

        assertEquals(1, result.size());
        verify(accountGameRepository).findByUserId_IdAndStatusIsTrueAndRealmNameAndUsername(
                realmName, userId, username, pageable);
    }

    @Test
    void findByUserIdAndRealmId_shouldReturnList() {
        Long userId = 1L, realmId = 99L;
        List<AccountGameEntity> expected = List.of(new AccountGameEntity());

        when(accountGameRepository.findByUserId_IdAndRealmId_IdAndStatusIsTrue(userId, realmId))
                .thenReturn(expected);

        List<AccountGameEntity> result = adapter.findByUserIdAndRealmId(userId, realmId, "tx123");

        assertEquals(1, result.size());
        verify(accountGameRepository).findByUserId_IdAndRealmId_IdAndStatusIsTrue(userId, realmId);
    }

    @Test
    void save_shouldReturnSavedEntity() {
        AccountGameEntity entity = new AccountGameEntity();
        when(accountGameRepository.save(entity)).thenReturn(entity);

        AccountGameEntity result = adapter.save(entity, "tx123");

        assertEquals(entity, result);
        verify(accountGameRepository).save(entity);
    }
}
