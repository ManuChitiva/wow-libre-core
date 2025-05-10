package com.register.wowlibre.application.services.machine;

import com.register.wowlibre.application.services.*;
import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.machine.*;
import com.register.wowlibre.domain.port.out.machine.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class MachineService implements MachinePort {
    private final AccountGamePort accountGamePort;
    private final ObtainMachine obtainMachine;
    private final SaveMachine saveMachine;
    private final IntegratorPort integratorPort;
    private final I18nService i18nService;

    public MachineService(AccountGamePort accountGamePort, ObtainMachine obtainMachine, SaveMachine saveMachine,
                          IntegratorPort integratorPort, I18nService i18nService) {
        this.accountGamePort = accountGamePort;
        this.obtainMachine = obtainMachine;
        this.saveMachine = saveMachine;
        this.integratorPort = integratorPort;
        this.i18nService = i18nService;
    }

    @Override
    public MachineDto evaluate(Long userId, Long accountId, Long characterId, Long realmId, Locale locale,
                               String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, realmId,
                transactionId);

        final RealmEntity realm = verificationDto.realm();

        int[] weights = {21, 1, 5, 3, 70};
        String[] outcomes = {"Item", "Level", "Mount", "Gold", "None"};

        Optional<MachineEntity> machine = obtainMachine.findByUserIdAndRealmId(userId,
                verificationDto.realm().getId());

        MachineEntity machineModel = new MachineEntity();

        if (machine.isPresent()) {
            machineModel = machine.get();

            if (machineModel.getPoints() <= 0) {
                throw new InternalException("You do not have the necessary points", transactionId);
            }

            machineModel.setPoints(Math.max(machineModel.getPoints() - 1, 0));
        } else {
            machineModel.setPoints(1);
            machineModel.setUserId(userId);
            machineModel.setRealmId(null);
            saveMachine.save(machineModel, transactionId);
        }

        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }


        int randomNumber = ThreadLocalRandom.current().nextInt(totalWeight);


        String result = "";
        int cumulativeWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (randomNumber < cumulativeWeight) {
                result = outcomes[i];
                break;
            }
        }


        MachineType machineType = MachineType.getName(result);

        if (machineType == null) {
            saveMachine.save(machineModel, transactionId);
            return MachineDto.builder().winner(false).message(i18nService.tr("message-machine-loss", locale)).build();
        }

        ClaimMachineResponse claimMachineResponse = integratorPort.claimMachine(realm.getHost(), realm.getJwt(),
                userId, accountId, characterId,
                machineType.getName(), transactionId);

        if (!claimMachineResponse.isSend()) {
            return MachineDto.builder().winner(false).message(i18nService.tr("message-machine-failed", locale)).build();
        }

        machineModel.setLastWin(LocalDateTime.now());
        saveMachine.save(machineModel, transactionId);

        return MachineDto.builder().message(i18nService.tr("message-machine-winner", locale))
                .name(claimMachineResponse.getName())
                .logo(claimMachineResponse.getLogo()).winner(true).build();
    }

    @Override
    public MachineDetailDto points(Long userId, Long accountId, Long realmId, String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, realmId,
                transactionId);

        final RealmEntity realm = verificationDto.realm();

        Optional<MachineEntity> machine = obtainMachine.findByUserIdAndRealmId(userId, realmId);

        if (machine.isEmpty()) {
            MachineEntity machineModel = new MachineEntity();
            machineModel.setPoints(0);
            machineModel.setUserId(userId);
            machineModel.setRealmId(realm);
            saveMachine.save(machineModel, transactionId);
        }

        return machine.map(machineEntity ->
                new MachineDetailDto(machineEntity.getPoints())).orElse(new MachineDetailDto(0));
    }
}
