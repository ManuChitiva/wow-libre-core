package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "vote_wallet")
public class VoteWalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private UserEntity userId;
    @JoinColumn(
            name = "platform_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private VotingPlatformsEntity platformId;
    @Column(name = "vote_balance")
    private Integer voteBalance;
    @Column(name = "total_votes")
    private Integer totalVotes;
    @Column(name = "reference_code")
    private String referenceCode;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
