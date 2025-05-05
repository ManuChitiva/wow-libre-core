package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "promotion")
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    @Column(name = "img_url")
    private String imgUrl;
    private String name;
    private String description;
    @Column(name = "btn_text")
    private String btnText;
    @Column(name = "send_item")
    private boolean sendItem;
    @Column(name = "realm_id")
    private Long realmId;
    @Column(name = "min_level")
    private Integer minLevel;
    @Column(name = "max_level")
    private Integer maxLevel;
    private String type;
    private Double amount;
    @Column(name = "class_character")
    private Long classCharacter;
    private Integer level;
    private boolean status;
    private String language;
}
