package com.register.wowlibre.controller;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.port.in.machine.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


class MachineControllerTest {
    private MachineController machineController;

    @Mock
    private MachinePort machinePort;

    private final String TRANSACTION_ID = "txn-001";
    private final Long USER_ID = 1L;
    private final Long ACCOUNT_ID = 2L;
    private final Long SERVER_ID = 3L;
    private final Long CHARACTER_ID = 4L;

    private ClaimMachineDto claimRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        machineController = new MachineController(machinePort);

        // Inicializamos el DTO de la solicitud
        claimRequest = new ClaimMachineDto();
        claimRequest.setAccountId(ACCOUNT_ID);
        claimRequest.setServerId(SERVER_ID);
        claimRequest.setCharacterId(CHARACTER_ID);
    }

    @Test
    void shouldReturnMachinePoints() {
        // Arrange: Mockeamos la respuesta del servicio
        MachineDetailDto mockResponse = new MachineDetailDto(150);
        when(machinePort.points(eq(USER_ID), eq(ACCOUNT_ID), eq(SERVER_ID), eq(TRANSACTION_ID)))
                .thenReturn(mockResponse);

        // Act: Llamamos al método directamente
        GenericResponse<MachineDetailDto> response = machineController.points(
                TRANSACTION_ID, USER_ID, ACCOUNT_ID, SERVER_ID).getBody();

        // Assert: Verificamos que la respuesta sea correcta
        assertNotNull(response);
        assertEquals(150, response.getData().getCoins());
        verify(machinePort).points(eq(USER_ID), eq(ACCOUNT_ID), eq(SERVER_ID), eq(TRANSACTION_ID));
    }

    @Test
    void shouldEvaluateMachine() {
        // Arrange: Mockeamos la respuesta del servicio
        MachineDto mockMachineDto = MachineDto.builder()
                .logo("logo.jpg")
                .name("Mega Slot")
                .type("SLOT")
                .winner(true)
                .message("You won 100 coins!")
                .build();

        when(machinePort.evaluate(eq(USER_ID), eq(ACCOUNT_ID), eq(CHARACTER_ID), eq(SERVER_ID),
                any(), eq(TRANSACTION_ID))).thenReturn(mockMachineDto);

        // Act: Llamamos al método directamente
        GenericResponse<MachineDto> response = machineController.machine(
                TRANSACTION_ID, USER_ID, Locale.US, claimRequest).getBody();

        // Assert: Verificamos que la respuesta sea correcta
        assertNotNull(response);
        assertEquals("Mega Slot", response.getData().getName());
        assertTrue(response.getData().isWinner());
        assertEquals("You won 100 coins!", response.getData().getMessage());
        verify(machinePort).evaluate(eq(USER_ID), eq(ACCOUNT_ID), eq(CHARACTER_ID), eq(SERVER_ID),
                any(), eq(TRANSACTION_ID));
    }
}
