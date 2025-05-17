package com.register.wowlibre.domain.port.out.faqs;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainFaqs {
    List<FaqsEntity> findByTypeAndLanguage(FaqType type, String language);
}
