package com.register.wowlibre.infrastructure.repositories.benefit_guild;

import com.register.wowlibre.domain.port.out.benefit_guild.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaBenefitGuildAdapter implements ObtainBenefitGuild, SaveBenefitGuild {

    private final BenefitGuildRepository benefitGuildRepository;

    public JpaBenefitGuildAdapter(BenefitGuildRepository benefitGuildRepository) {
        this.benefitGuildRepository = benefitGuildRepository;
    }

    @Override
    public List<BenefitGuildEntity> findByServerIdAndGuildIdAndStatusIsTrue(Long serverId, Long guildId,
                                                                            String transactionId) {
        return benefitGuildRepository.findByRealmId_idAndGuildIdAndStatusIsTrue(serverId, guildId);
    }

    @Override
    public List<BenefitGuildEntity> findRemainingBenefitsForGuildAndServerIdAndCharacter(Long serverId, Long guildId,
                                                                                         Long characterId,
                                                                                         Long accountId,
                                                                                         String transactionId) {
        return benefitGuildRepository.findRemainingBenefitsForGuildAndServerIdAndCharacter(serverId, guildId,
                characterId, accountId);
    }

    @Override
    public void save(BenefitGuildEntity benefitGuild, String transactionId) {
        benefitGuildRepository.save(benefitGuild);
    }
}
