package com.register.wowlibre.domain.dto.faqs;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CreateFaqDto {
    @NotNull
    private String question;
    @NotNull
    private String answer;
    @NotNull
    private String type;
    @NotNull
    private String language;
}
