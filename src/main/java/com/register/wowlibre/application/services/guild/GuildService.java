package com.register.wowlibre.application.services.guild;

import com.register.wowlibre.domain.dto.account_game.*;
import com.register.wowlibre.domain.dto.client.*;
import com.register.wowlibre.domain.dto.guilds.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.mapper.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.benefit_guild.*;
import com.register.wowlibre.domain.port.in.character_benefit_guild.*;
import com.register.wowlibre.domain.port.in.guild.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class GuildService implements GuildPort {

    private final IntegratorPort integratorPort;
    private final RealmPort realmPort;
    private final ResourcesPort resourcesPort;
    private final BenefitGuildPort benefitGuildPort;
    private final AccountGamePort accountGamePort;
    private final CharacterBenefitGuildPort characterBenefitGuildPort;

    public GuildService(IntegratorPort integratorPort, RealmPort realmPort, ResourcesPort resourcesPort,
                        BenefitGuildPort benefitGuildPort, AccountGamePort accountGamePort,
                        CharacterBenefitGuildPort characterBenefitGuildPort) {
        this.integratorPort = integratorPort;
        this.realmPort = realmPort;
        this.resourcesPort = resourcesPort;
        this.benefitGuildPort = benefitGuildPort;
        this.accountGamePort = accountGamePort;
        this.characterBenefitGuildPort = characterBenefitGuildPort;
    }

    @Override
    public GuildsDto findAll(Integer size, Integer page, String search, String realmName, Integer expansionId,
                             String transactionId) {
        RealmModel server;

        if (realmName != null && !realmName.isEmpty() && expansionId != null) {
            server = realmPort.findByNameAndVersionAndStatusIsTrue(realmName, expansionId, transactionId);

            if (server == null) {
                return new GuildsDto(new ArrayList<>(), 0L);
            }

        } else {

            Optional<RealmEntity> realms = realmPort.findByStatusIsTrueServers(transactionId).stream().findFirst();

            if (realms.isEmpty()) {
                return new GuildsDto(new ArrayList<>(), 0L);
            }

            server = RealmMapper.toModel(realms.get());
        }


        return integratorPort.guilds(server.name, server.id, server.ip, server.jwt, size, page, search, transactionId);
    }

    @Override
    public GuildDto detail(Long realmId, Long guildId, Locale locale, String transactionId) {

        Optional<RealmEntity> realm = realmPort.findById(realmId, transactionId);

        if (realm.isEmpty() || !realm.get().isStatus()) {
            throw new InternalException("The Realm is not available", transactionId);
        }

        RealmEntity realmDetail = realm.get();

        GuildDto guildDto = integratorPort.guild(realmDetail.getName(), realmDetail.getId(), realmDetail.getHost(),
                realmDetail.getJwt(), guildId,
                transactionId);

        List<BenefitModel> beneficios = resourcesPort.getBenefitsGuild(locale.getLanguage(), transactionId);

        List<BenefitGuildEntity> beneficiosObtenidos = benefitGuildPort.benefits(realmDetail.getId(), guildId,
                transactionId);

        List<BenefitModel> beneficiosFiltrados = beneficios.stream()
                .filter(beneficio -> beneficiosObtenidos.stream()
                        .anyMatch(beneficioObtenido -> beneficioObtenido.getBenefitId().equals(beneficio.id)))
                .toList();

        guildDto.setBenefits(beneficiosFiltrados);

        return guildDto;
    }

    @Override
    public void attach(Long serverId, Long userId, Long accountId, Long characterId, Long guildId,
                       String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        integratorPort.attachGuild(verificationDto.realm().getHost(), verificationDto.realm().getJwt(),
                verificationDto.accountGame().getAccountId(), guildId, characterId, transactionId);

    }

    @Override
    public void unInviteGuild(Long serverId, Long userId, Long accountId, Long characterId, String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        integratorPort.unInviteGuild(verificationDto.realm().getHost(), verificationDto.realm().getJwt(), userId,
                accountId, characterId, transactionId);
    }

    @Override
    public GuildMemberDetailDto guildMember(Long serverId, Long userId, Long accountId, Long characterId,
                                            String transactionId) {
        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        GuildDetailMemberResponse response = integratorPort.guildMember(verificationDto.realm().getHost(),
                verificationDto.realm().getJwt(), userId,
                accountId, characterId, transactionId);

        Integer benefits = benefitGuildPort.findRemainingBenefitsForGuildAndServerIdAndCharacter(serverId,
                response.getId(), characterId, accountId, transactionId).size();

        return GuildMemberDetailDto.builder()
                .id(response.getId())
                .name(response.getName())
                .leaderName(response.getLeaderName())
                .emblemColor(response.getEmblemColor())
                .borderStyle(response.getBorderStyle())
                .borderColor(response.getBorderColor())
                .info(response.getInfo())
                .motd(response.getMotd())
                .isLeader(response.getIsLeader())
                .multiFaction(response.getMultiFaction())
                .discord(response.getDiscord())
                .createDate(response.getCreateDate())
                .bankMoney(response.getBankMoney())
                .members(response.getMembers())
                .publicAccess(response.isPublicAccess())
                .availableBenefits(benefits)
                .formattedBankMoney(response.getFormattedBankMoney())
                .members(response.getMembers()).build();
    }

    @Override
    public void update(Long serverId, Long userId, Long accountId, Long characterId, String discord,
                       boolean multiFaction, boolean isPublic, String transactionId) {
        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        integratorPort.updateGuild(verificationDto.realm().getHost(),
                verificationDto.realm().getJwt(), characterId, accountId, isPublic, multiFaction, discord,
                transactionId);
    }

    @Override
    public void claimBenefits(Long serverId, Long userId, Long accountId, Long characterId,
                              String language,
                              String transactionId) {

        AccountVerificationDto verificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        RealmEntity serverModel = verificationDto.realm();

        GuildDetailMemberResponse guildDto = integratorPort.guildMember(verificationDto.realm().getHost(),
                verificationDto.realm().getJwt(), userId,
                accountId, characterId, transactionId);

        if (guildDto == null) {
            throw new InternalException("You do not belong to a guild or an unexpected error has occurred",
                    transactionId);
        }

        List<BenefitModel> beneficios = resourcesPort.getBenefitsGuild(language, transactionId);

        if (beneficios.isEmpty()) {
            throw new InternalException("There are no benefits available to purchase", transactionId);
        }

        List<BenefitGuildEntity> benefitsGuild =
                benefitGuildPort.findRemainingBenefitsForGuildAndServerIdAndCharacter(serverId,
                        guildDto.getId(), characterId, accountId, transactionId);

        if (benefitsGuild.isEmpty()) {
            throw new InternalException("There are no benefits available to purchase", transactionId);
        }

        List<ItemQuantityModel> filteredBenefits = beneficios.stream()
                .filter(benefit ->
                        benefitsGuild.stream()
                                .anyMatch(benefitGuild ->
                                        benefitGuild.getBenefitId().equals(benefit.id)
                                )
                )
                .map(benefit -> new ItemQuantityModel(benefit.itemId, 1))
                .toList();


        if (filteredBenefits.isEmpty()) {
            throw new InternalException("There are no benefits available to purchase", transactionId);
        }

        integratorPort.sendGuildBenefit(serverModel.getHost(), serverModel.getJwt(), userId, accountId, characterId,
                filteredBenefits, transactionId);
        benefitsGuild.forEach(benefit -> characterBenefitGuildPort.save(characterId, accountId, benefit, true,
                transactionId));

    }
}
