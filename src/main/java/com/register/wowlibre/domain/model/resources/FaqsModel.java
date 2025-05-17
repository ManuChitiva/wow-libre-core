package com.register.wowlibre.domain.model.resources;

import com.register.wowlibre.domain.enums.*;

public record FaqsModel(Long id, String question, String answer, FaqType type) {
}
