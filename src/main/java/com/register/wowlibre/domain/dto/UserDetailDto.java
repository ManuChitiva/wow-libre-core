package com.register.wowlibre.domain.dto;

import lombok.*;

import java.time.*;

@Getter
@Builder
public class UserDetailDto {
    private Long id;
    private String country;
    private LocalDate dateOfBirth;
    private String firstName;
    private String lastName;
    private String cellPhone;
    private String email;
    private String rolName;
    private boolean status;
    private boolean verified;
}
