package com.register.wowlibre.domain.dto.account_game;

import com.register.wowlibre.domain.model.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class AccountsGameDto {
    private List<AccountGameModel> accounts;
    private Long size;
}
