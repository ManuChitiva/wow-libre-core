package com.register.wowlibre.application.services.resources;


import com.register.wowlibre.domain.dto.faqs.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.port.out.*;
import com.register.wowlibre.domain.port.out.faqs.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ResourcesService implements ResourcesPort {
    private final JsonLoaderPort jsonLoaderPort;
    private final ObtainFaqs obtainFaqs;
    private final SaveFaqs saveFaqs;

    public ResourcesService(JsonLoaderPort jsonLoaderPort, ObtainFaqs obtainFaqs, SaveFaqs saveFaqs) {
        this.jsonLoaderPort = jsonLoaderPort;
        this.obtainFaqs = obtainFaqs;
        this.saveFaqs = saveFaqs;
    }

    @Override
    public List<CountryModel> getCountry(String transactionId) {
        return jsonLoaderPort.getJsonCountry(transactionId);
    }

    @Override
    public List<FaqsModel> getFaqs(FaqType type, String language, String transactionId) {
        return obtainFaqs.findByTypeAndLanguage(type, language).stream()
                .map(faqsEntity -> new FaqsModel(faqsEntity.getId(), faqsEntity.getQuestion(),
                        faqsEntity.getAnswer(), faqsEntity.getType())).toList();
    }

    @Override
    public void createFaq(CreateFaqDto createFaq, String transactionId) {
        FaqType faqType = FaqType.getByName(createFaq.getType());

        if (faqType == null) {
            throw new InternalException("Invalid FAQ type: ", transactionId);
        }

        FaqsEntity faqsEntity = new FaqsEntity();
        faqsEntity.setQuestion(createFaq.getQuestion());
        faqsEntity.setAnswer(createFaq.getAnswer());
        faqsEntity.setType(faqType);
        faqsEntity.setLanguage(createFaq.getLanguage());
        saveFaqs.save(faqsEntity, transactionId);
    }

    @Override
    public void deleteFaq(Long faqId, String transactionId) {
        saveFaqs.delete(faqId, transactionId);
    }


    @Override
    public List<PlanModel> getPlansBank(String language, String transactionId) {
        return jsonLoaderPort.getJsonPlans(language, transactionId);
    }

    @Override
    public List<BenefitModel> getBenefitsGuild(String language, String transactionId) {
        return jsonLoaderPort.getJsonBenefitsGuild(language, transactionId);
    }


    @Override
    public WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId) {
        return jsonLoaderPort.getWidgetSubscription(language, transactionId);
    }

    @Override
    public List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId) {
        return jsonLoaderPort.getPlansAcquisition(language, transactionId);
    }

}
