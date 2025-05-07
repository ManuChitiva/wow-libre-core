package com.register.wowlibre.domain.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class AssociatedRealm {
    private List<RealmDto> realms;
    private Integer size;
}
