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
    private String img;
    private String name;
    private String description;
    @Column(name = "btn_text")
    private String btnText;
    @Column(name = "send_item")
    private boolean sendItem;
    @Column(name = "server_id")
    private Long serverId;
    @Column(name = "min_level")
    private Integer minLevel;
    @Column(name = "max_level")
    private Integer maxLevel;
    private String type;
    private Double amount;
    @Column(name = "class_character")
    private Long classCharacter;
    private String language;
    private boolean status;
    private Integer level;
}
