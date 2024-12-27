package com.register.wowlibre.domain.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class AssociatedServers {
    private List<ServerDto> servers;
    private Integer size;
}
