package com.register.wowlibre.domain.port.out.faqs;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainFaqs {
    List<FaqsEntity> findByLanguage(String language);
}
