package com.register.wowlibre.service;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.application.services.machine.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.out.machine.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MachineServiceTest {
    @Mock
    private AccountGamePort accountGamePort;

    @Mock
    private ObtainMachine obtainMachine;

    @Mock
    private SaveMachine saveMachine;

    @Mock
    private IntegratorPort integratorPort;

    @Mock
    private I18nService i18nService;

    @InjectMocks
    private MachineService machineService;

    private final String transactionId = "txn-123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AccountVerificationDto createAccountVerificationDto(Long realmId) {
        RealmEntity realm = new RealmEntity();
        realm.setId(realmId);
        realm.setHost("localhost");
        realm.setJwt("jwt-token");

        return new AccountVerificationDto(realm, null);
    }


    @Test
    void evaluate_ShouldThrowException_WhenUserHasNoPoints() {
        Long userId = 1L, accountId = 2L, characterId = 3L, realmId = 4L;
        AccountVerificationDto verificationDto = createAccountVerificationDto(realmId);

        MachineEntity machine = new MachineEntity();
        machine.setUserId(userId);
        machine.setPoints(0);
        machine.setRealmId(verificationDto.realm());

        when(accountGamePort.verifyAccount(userId, accountId, realmId, transactionId))
                .thenReturn(verificationDto);
        when(obtainMachine.findByUserIdAndRealmId(userId, realmId)).thenReturn(Optional.of(machine));

        assertThrows(InternalException.class, () ->
                machineService.evaluate(userId, accountId, characterId, realmId, Locale.ENGLISH, transactionId)
        );
    }

    @Test
    void evaluate_ShouldReturnLoss_WhenClaimFails() {
        Long userId = 1L, accountId = 2L, characterId = 3L, realmId = 4L;
        AccountVerificationDto verificationDto = createAccountVerificationDto(realmId);

        MachineEntity machine = new MachineEntity();
        machine.setUserId(userId);
        machine.setPoints(1);
        machine.setRealmId(verificationDto.realm());

        when(accountGamePort.verifyAccount(userId, accountId, realmId, transactionId))
                .thenReturn(verificationDto);
        when(obtainMachine.findByUserIdAndRealmId(userId, realmId)).thenReturn(Optional.of(machine));
        when(integratorPort.claimMachine(any(), any(), eq(userId), eq(accountId), eq(characterId), any(),
                eq(transactionId)))
                .thenReturn(new ClaimMachineResponse(false));
        when(i18nService.tr(any(), any())).thenReturn("Claim failed");

        MachineDto result = machineService.evaluate(userId, accountId, characterId, realmId, Locale.ENGLISH,
                transactionId);

        assertFalse(result.isWinner());
        assertEquals("Claim failed", result.getMessage());
    }

    @Test
    void points_ShouldReturnInitialPoints_WhenMachineNotExist() {
        Long userId = 1L, accountId = 2L, realmId = 3L;
        AccountVerificationDto verificationDto = createAccountVerificationDto(realmId);

        when(accountGamePort.verifyAccount(userId, accountId, realmId, transactionId)).thenReturn(verificationDto);
        when(obtainMachine.findByUserIdAndRealmId(userId, realmId)).thenReturn(Optional.empty());

        MachineDetailDto detailDto = machineService.points(userId, accountId, realmId, transactionId);

        assertEquals(0, detailDto.getCoins());
        verify(saveMachine).save(any(), eq(transactionId));
    }

    @Test
    void points_ShouldReturnExistingPoints_WhenMachineExists() {
        Long userId = 1L, accountId = 2L, realmId = 3L;
        AccountVerificationDto verificationDto = createAccountVerificationDto(realmId);

        MachineEntity machine = new MachineEntity();
        machine.setPoints(3);

        when(accountGamePort.verifyAccount(userId, accountId, realmId, transactionId)).thenReturn(verificationDto);
        when(obtainMachine.findByUserIdAndRealmId(userId, realmId)).thenReturn(Optional.of(machine));

        MachineDetailDto detailDto = machineService.points(userId, accountId, realmId, transactionId);

        assertEquals(3, detailDto.getCoins());
    }
}
