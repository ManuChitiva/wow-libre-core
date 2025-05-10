package com.register.wowlibre.repository;

import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.repositories.machine.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaMachineAdapterTest {
    @Mock
    private MachineRepository machineRepository;

    @InjectMocks
    private JpaMachineAdapter jpaMachineAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserIdAndRealmId_returnsMachine() {
        // Given
        Long userId = 1L;
        Long realmId = 2L;
        MachineEntity machine = new MachineEntity();
        machine.setId(10L);
        machine.setUserId(userId);
        machine.setPoints(100);
        machine.setLastWin(LocalDateTime.now());
        RealmEntity realmEntity = new RealmEntity();
        realmEntity.setId(realmId);
        machine.setRealmId(realmEntity);

        when(machineRepository.findByUserIdAndRealmId_Id(userId, realmId)).thenReturn(Optional.of(machine));

        // When
        Optional<MachineEntity> result = jpaMachineAdapter.findByUserIdAndRealmId(userId, realmId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(machine.getId(), result.get().getId());
        verify(machineRepository).findByUserIdAndRealmId_Id(userId, realmId);
    }

    @Test
    void testFindByUserIdAndRealmId_returnsEmpty() {
        // Given
        Long userId = 1L;
        Long realmId = 2L;

        when(machineRepository.findByUserIdAndRealmId_Id(userId, realmId)).thenReturn(Optional.empty());

        // When
        Optional<MachineEntity> result = jpaMachineAdapter.findByUserIdAndRealmId(userId, realmId);

        // Then
        assertFalse(result.isPresent());
        verify(machineRepository).findByUserIdAndRealmId_Id(userId, realmId);
    }

    @Test
    void testSaveMachine_callsRepositorySave() {
        // Given
        MachineEntity machine = new MachineEntity();
        machine.setId(1L);
        String transactionId = "txn-123";

        // When
        jpaMachineAdapter.save(machine, transactionId);

        // Then
        verify(machineRepository).save(machine);
    }

}
