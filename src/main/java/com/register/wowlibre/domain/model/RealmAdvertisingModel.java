package com.register.wowlibre.domain.model;

import lombok.*;

@Builder
public class RealmAdvertisingModel {
    public final Long id;
    public final String title;
    public final String tag;
    public final String subTitle;
    public final String description;
    public final String ctaPrimary;
    public final String imgUrl;
    public final boolean copySuccess;
    public final String redirect;
    public final String footerDisclaimer;
    public final String realmlist;
}
