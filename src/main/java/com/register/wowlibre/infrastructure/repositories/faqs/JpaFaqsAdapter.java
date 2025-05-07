package com.register.wowlibre.infrastructure.repositories.faqs;

import com.register.wowlibre.domain.port.out.faqs.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaFaqsAdapter implements ObtainFaqs {
    private final FaqsRepository faqsRepository;

    public JpaFaqsAdapter(FaqsRepository faqsRepository) {
        this.faqsRepository = faqsRepository;
    }

    @Override
    public List<FaqsEntity> findByLanguage(String language) {
        return faqsRepository.findByLanguage(language);
    }
}
