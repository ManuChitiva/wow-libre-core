package com.register.wowlibre.service;

import com.register.wowlibre.application.services.rol.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.out.rol.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceTest {
    @Mock
    private ObtainRolPort obtainRolPort;

    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rolService = new RolService(obtainRolPort);
    }

    private RolEntity createSampleRolEntity() {
        RolEntity entity = new RolEntity();
        entity.setId(1L);
        entity.setName("CLIENT");
        entity.setStatus(true);
        return entity;
    }

    @Test
    void findByName_shouldReturnRolModel_whenRolExists() {
        String name = "CLIENT";
        String txId = "tx123";
        RolEntity entity = createSampleRolEntity();

        when(obtainRolPort.findByName(name)).thenReturn(Optional.of(entity));

        RolModel result = rolService.findByName(name, txId);

        assertNotNull(result);
        assertEquals("CLIENT", result.name());
        assertTrue(result.status());
        verify(obtainRolPort).findByName(name);
    }

    @Test
    void findByName_shouldReturnNull_whenRolDoesNotExist() {
        when(obtainRolPort.findByName("ADMIN")).thenReturn(Optional.empty());

        RolModel result = rolService.findByName("ADMIN", "tx456");

        assertNull(result);
        verify(obtainRolPort).findByName("ADMIN");
    }
}
