package com.register.wowlibre.domain.model.resources;

import com.fasterxml.jackson.annotation.*;


public record ExperiencesHomeModel(String title, @JsonProperty("title_disclaimer") String titleDisclaimer,
                                   String subtitle, String description,
                                   @JsonProperty("button_primary_text") String buttonPrimaryText,
                                   @JsonProperty("button_secondary_text") String buttonSecondaryText,
                                   @JsonProperty("background_image") String backgroundImage, String realmlist,
                                   @JsonProperty("copy_success") Boolean copySuccess, String redirect,
                                   String disclaimer) {
}
