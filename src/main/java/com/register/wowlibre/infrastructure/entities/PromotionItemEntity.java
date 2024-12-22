package com.register.wowlibre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "promotion_item")
public class PromotionItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer quantity;
    @JoinColumn(
            name = "promotion_id",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private PromotionEntity promotionId;
}
