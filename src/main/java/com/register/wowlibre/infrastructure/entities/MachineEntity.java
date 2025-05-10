package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "machine")
public class MachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private Integer points;
    @Column(name = "last_win")
    private LocalDateTime lastWin;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY)
    private RealmEntity realmId;
}
