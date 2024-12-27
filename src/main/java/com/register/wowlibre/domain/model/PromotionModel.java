package com.register.wowlibre.domain.model;

import lombok.*;

import java.util.*;

@Data
@Builder
public class PromotionModel {
    private Long id;
    private String reference;
    private String img;
    private String name;
    private String description;
    private String btnTxt;
    private Boolean sendItem;
    private String type;
    private Integer minLvl;
    private Integer maxLvl;
    private List<Items> items;
    private Double amount;
    private Long serverId;
    private Long classId;
    private boolean status;
    private Integer level;


    @AllArgsConstructor
    public static class Items {
        public String code;
        public Integer quantity;
    }
}
