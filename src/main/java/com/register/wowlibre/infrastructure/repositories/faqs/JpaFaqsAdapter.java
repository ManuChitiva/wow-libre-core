package com.register.wowlibre.infrastructure.repositories.faqs;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.port.out.faqs.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaFaqsAdapter implements ObtainFaqs, SaveFaqs {
    private final FaqsRepository faqsRepository;

    public JpaFaqsAdapter(FaqsRepository faqsRepository) {
        this.faqsRepository = faqsRepository;
    }

    @Override
    public List<FaqsEntity> findByTypeAndLanguage(FaqType type, String language) {
        return faqsRepository.findByTypeAndLanguage(type, language);
    }

    @Override
    public void save(FaqsEntity faqsEntity, String transactionId) {
        faqsRepository.save(faqsEntity);
    }

    @Override
    public void delete(Long faqId, String transactionId) {
        faqsRepository.findById(faqId).ifPresent(faqsRepository::delete);
    }
}
