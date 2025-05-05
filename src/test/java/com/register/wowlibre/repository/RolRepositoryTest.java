package com.register.wowlibre.repository;

import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.repositories.rol.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class RolRepositoryTest {
    @Mock
    private RolRepository rolRepository;

    private JpaRolAdapter jpaRolAdapter;

    @BeforeEach
    void setUp() {
        openMocks(this);
        jpaRolAdapter = new JpaRolAdapter(rolRepository);
    }

    @Test
    void findByName_shouldReturnRoleWhenFound() {
        // Arrange
        String roleName = "ADMIN";
        String transactionId = "tx123";
        RolEntity mockRol = new RolEntity();
        mockRol.setId(1L);
        mockRol.setName(roleName);
        mockRol.setStatus(true);

        when(rolRepository.findByNameAndStatusIsTrue(roleName)).thenReturn(Optional.of(mockRol));

        // Act
        Optional<RolEntity> result = jpaRolAdapter.findByName(roleName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(roleName, result.get().getName());
        verify(rolRepository, times(1)).findByNameAndStatusIsTrue(roleName);
    }

    @Test
    void findByName_shouldReturnEmptyWhenRoleNotFound() {
        // Arrange
        String roleName = "UNKNOWN";
        String transactionId = "tx456";

        when(rolRepository.findByNameAndStatusIsTrue(roleName)).thenReturn(Optional.empty());

        // Act
        Optional<RolEntity> result = jpaRolAdapter.findByName(roleName);

        // Assert
        assertFalse(result.isPresent());
        verify(rolRepository, times(1)).findByNameAndStatusIsTrue(roleName);
    }
}
