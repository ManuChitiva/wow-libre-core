package com.register.wowlibre.infrastructure.entities;

import com.register.wowlibre.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "teleport")
@NoArgsConstructor
public class TeleportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "img_url")
    private String imgUrl;
    private String name;
    @Column(name = "position_x")
    private Double positionX;
    @Column(name = "position_y")
    private Double positionY;
    @Column(name = "position_z")
    private Double positionZ;
    private Integer map;
    private Double orientation;
    private Integer zone;
    private Double area;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Faction faction;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private RealmEntity realmId;
}
