package com.register.wowlibre.domain.model;

import java.util.*;

public record PlanAcquisitionModel(String name, String price, String description, List<String> features,
                                   String buttonText, String url) {
}
