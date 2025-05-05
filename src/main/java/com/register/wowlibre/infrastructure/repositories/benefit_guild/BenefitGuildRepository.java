package com.register.wowlibre.infrastructure.repositories.benefit_guild;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface BenefitGuildRepository extends CrudRepository<BenefitGuildEntity, Long> {
    List<BenefitGuildEntity> findByRealmId_idAndGuildIdAndStatusIsTrue(Long serverId, Long guildId);

    // Consulta para obtener los beneficios restantes de la guild
    @Query("SELECT bg FROM BenefitGuildEntity bg " +
            "LEFT JOIN CharacterBenefitGuildEntity cbg ON cbg.benefitGuildId.id = bg.id " +
            "   AND cbg.benefitSend = true " +
            "   AND cbg.characterId = :characterId " +
            "   AND cbg.accountId = :accountId " +
            "WHERE bg.guildId = :guildId " +
            "  AND bg.realmId.id = :serverId " +
            "  AND cbg.id IS NULL")
    List<BenefitGuildEntity> findRemainingBenefitsForGuildAndServerIdAndCharacter(
            @Param("serverId") Long serverId,
            @Param("guildId") Long guildId,
            @Param("characterId") Long characterId,
            @Param("accountId") Long accountId
    );

}
