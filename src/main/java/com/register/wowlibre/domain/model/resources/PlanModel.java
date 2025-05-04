package com.register.wowlibre.domain.model.resources;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public record PlanModel(Long id, String name, String description, String price, String frecuency, List<String> features,
                        String button, Integer interest,
                        @JsonProperty("month_payment_period") Integer monthPaymentPeriod, Double gold) {

    public PlanModel(Long id, String name, String description, String price, String frecuency, List<String> features,
                     String button, Integer interest, Integer monthPaymentPeriod, Double gold) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.frecuency = frecuency;
        this.features = features;
        this.button = button;
        this.interest = interest;
        this.monthPaymentPeriod = monthPaymentPeriod;
        this.gold = gold;
    }


}
