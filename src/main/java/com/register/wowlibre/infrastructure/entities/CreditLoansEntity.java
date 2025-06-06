package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "credit_loans")
public class CreditLoansEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(
            name = "account_game_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private AccountGameEntity accountGameId;
    @Column(name = "character_id")
    private Long characterId;
    @Column(name = "realm_id")
    private Long realmId;
    @Column(name = "reference_serial")
    private String referenceSerial;
    private Double amountTransferred;
    private Double debtToPay;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    private Integer interests;
    private boolean status;
    private boolean send;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
