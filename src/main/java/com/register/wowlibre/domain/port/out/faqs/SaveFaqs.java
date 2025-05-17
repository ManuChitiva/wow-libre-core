package com.register.wowlibre.domain.port.out.faqs;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveFaqs {
    void save(FaqsEntity faqsEntity, String transactionId);

    void delete(Long faqId, String transactionId);
}
