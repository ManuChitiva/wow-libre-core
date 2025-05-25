package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "realm_advertising")
public class RealmAdvertisingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;
    @Column(name = "sub_title")
    private String subTitle;
    @Column(name = "description")
    private String description;
    @Column(name = "cta_primary")
    private String ctaPrimary;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "footer_disclaimer")
    private String footerDisclaimer;
    private String language;
    @JoinColumn(
            name = "realm_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY)
    private RealmEntity realmId;
}
