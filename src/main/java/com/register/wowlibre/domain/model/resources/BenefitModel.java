package com.register.wowlibre.domain.model.resources;

import com.fasterxml.jackson.annotation.*;

public class BenefitModel {

    public final Long id;
    public final String title;
    @JsonProperty("sub_title")
    public final String subTitle;
    public final String description;
    public final String logo;
    @JsonProperty("item_id")
    public final String itemId;
    public final Integer quantity;
    public final boolean status;
    public final String link;

    public BenefitModel(Long id, String title, String subTitle, String description, String logo, String itemId, Integer quantity, boolean status, String link) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.logo = logo;
        this.itemId = itemId;
        this.quantity = quantity;
        this.status = status;
        this.link = link;
    }
}
