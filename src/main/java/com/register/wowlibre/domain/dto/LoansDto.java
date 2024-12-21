package com.register.wowlibre.domain.dto;

import lombok.*;

import java.time.*;
import java.util.*;

@Data
public class LoansDto {
    private Double loans;
    private List<UsersCreditLoans> users;

    @Data
    @AllArgsConstructor
    public static class UsersCreditLoans {
        private Long id;
        private String name;
        private LocalDateTime applicationDate;
        private boolean debtor;
        private LocalDateTime paymentDate;
        private Double amount;

    }
}
