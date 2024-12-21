package com.register.wowlibre.domain.dto;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public class AccountsGameDto {
    private List<AccountGameDto> accounts;
    private Long size;
}
