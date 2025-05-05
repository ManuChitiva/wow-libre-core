package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;


@Data
@Entity
@Table(name = "account_game")
@NoArgsConstructor
public class AccountGameEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(name = "account_id")
    private Long accountId;
    private boolean status;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RealmEntity realmId;
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private UserEntity userId;
}
