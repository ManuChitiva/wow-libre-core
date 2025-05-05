package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.time.*;

@Data
@Entity
@Table(name = "server_details")
public class RealmDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String key;
    private String value;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY)
    private RealmEntity realmId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
