package com.register.wowlibre.infrastructure.entities;

import com.register.wowlibre.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "realm_services")
public class RealmServicesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RealmServices name;
    @Column(name = "amount")
    private Double amount;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RealmEntity realmId;
}
