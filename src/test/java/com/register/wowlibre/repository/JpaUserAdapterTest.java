package com.register.wowlibre.repository;

import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.repositories.user.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaUserAdapterTest {

    @Mock
    private UserRepository userRepository;

    private JpaUserAdapter jpaUserAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jpaUserAdapter = new JpaUserAdapter(userRepository);
    }

    @Test
    void findByEmailAndStatusIsTrue_shouldReturnUser_whenUserExists() {
        // Arrange
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setStatus(true);

        when(userRepository.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> result = jpaUserAdapter.findByEmailAndStatusIsTrue(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository, times(1)).findByEmailAndStatusIsTrue(email);
    }

    @Test
    void findByCellPhoneAndStatusIsTrue_shouldReturnUser_whenUserExists() {
        // Arrange
        String cellPhone = "123456789";
        String transactionId = "tx123";
        UserEntity user = new UserEntity();
        user.setCellPhone(cellPhone);
        user.setStatus(true);

        when(userRepository.findByCellPhoneAndStatusIsTrue(cellPhone)).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> result = jpaUserAdapter.findByCellPhoneAndStatusIsTrue(cellPhone, transactionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cellPhone, result.get().getCellPhone());
        verify(userRepository).findByCellPhoneAndStatusIsTrue(cellPhone);
    }

    @Test
    void findByUserIdAndStatusIsTrue_shouldReturnUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        String transactionId = "tx456";
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setStatus(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> result = jpaUserAdapter.findByUserIdAndStatusIsTrue(userId, transactionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void save_shouldPersistUser() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setEmail("save@example.com");

        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserEntity saved = jpaUserAdapter.save(user, user.getEmail());

        // Assert
        assertNotNull(saved);
        assertEquals("save@example.com", saved.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void findByEmailAndStatusIsTrue_shouldReturnEmpty_whenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";

        when(userRepository.findByEmailAndStatusIsTrue(email)).thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> result = jpaUserAdapter.findByEmailAndStatusIsTrue(email);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findByEmailAndStatusIsTrue(email);
    }
}
