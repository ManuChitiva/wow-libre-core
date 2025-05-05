package com.register.wowlibre.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserDto {
    @NotNull
    @Length(min = 2, max = 30)
    private String country;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Length(min = 3, max = 30)
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @Length(min = 3, max = 30)
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @Length(min = 5, max = 20)
    @JsonProperty("cell_phone")
    private String cellPhone;

    @NotNull
    @Length(min = 5, max = 50)
    @Email
    private String email;

    @Length(max = 30)
    @NotNull
    private String password;

    @NotNull
    @Length(min = 1, max = 4)
    private String language;

    @NotNull
    private String token;
}
