package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.time.*;

@Data
@Entity
@Table(name = "server_details")
public class ServerDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String key;
    private String value;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @JoinColumn(
            name = "server_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RealmEntity serverId;
}
