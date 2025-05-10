package com.register.wowlibre.domain.port.in.guild;

import com.register.wowlibre.domain.dto.guilds.*;

import java.util.*;

public interface GuildPort {
    GuildsDto findAll(Integer size, Integer page, String search, String serverName, Integer expansionId,
                      String transactionId);

    GuildDto detail(Long realmId, Long guildId, Locale locale, String transactionId);

    void attach(Long serverId, Long userId, Long accountId, Long characterId, Long guildId, String transactionId);

    void unInviteGuild(Long serverId, Long userId, Long accountId, Long characterId, String transactionId);

    GuildMemberDetailDto guildMember(Long serverId, Long userId, Long accountId, Long characterId,
                                     String transactionId);

    void update(Long serverId, Long userId, Long accountId, Long characterId, String discord, boolean multiFaction,
                boolean isPublic, String transactionId);

    void claimBenefits(Long serverId, Long userId, Long accountId, Long characterId, String language,
                       String transactionId);
}
