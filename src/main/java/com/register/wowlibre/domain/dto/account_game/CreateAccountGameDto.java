package com.register.wowlibre.domain.dto.account_game;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class CreateAccountGameDto {
    @NotNull
    @Length(min = 5, max = 20)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String serverName;
    @NotNull
    private Integer expansion;
}
