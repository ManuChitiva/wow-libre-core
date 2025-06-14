package com.register.wowlibre.infrastructure.entities;

import com.register.wowlibre.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "banners")
@NoArgsConstructor
public class BannersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "media_url")
    private String mediaUrl;
    private String alt;
    private String language;
    @Enumerated(EnumType.STRING)
    private BannerType type;
    private String label;
}
