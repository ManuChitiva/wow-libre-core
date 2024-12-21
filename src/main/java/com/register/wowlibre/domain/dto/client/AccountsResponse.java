package com.register.wowlibre.domain.dto.client;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
public class AccountsResponse {
    private List<AccountGameResponse> accounts;
    private Long size;
}
