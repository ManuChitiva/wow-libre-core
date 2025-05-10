package com.register.wowlibre.domain.dto.guilds;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class GuildsDto {
    private List<GuildDto> guilds;
    private Long size;
}
