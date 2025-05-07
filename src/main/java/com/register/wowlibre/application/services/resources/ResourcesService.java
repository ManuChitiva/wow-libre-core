package com.register.wowlibre.application.services.resources;


import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.port.out.*;
import com.register.wowlibre.domain.port.out.faqs.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ResourcesService implements ResourcesPort {
    private final JsonLoaderPort jsonLoaderPort;
    private final ObtainFaqs obtainFaqs;

    public ResourcesService(JsonLoaderPort jsonLoaderPort, ObtainFaqs obtainFaqs) {
        this.jsonLoaderPort = jsonLoaderPort;
        this.obtainFaqs = obtainFaqs;
    }

    @Override
    public List<BannerHomeModel> getBannersHome(String language, String transactionId) {
        return jsonLoaderPort.getBannersHome(language, transactionId);
    }

    @Override
    public List<CountryModel> getCountry(String transactionId) {
        return jsonLoaderPort.getJsonCountry(transactionId);
    }

    @Override
    public List<FaqsModel> getFaqs(String language, String transactionId) {
        return obtainFaqs.findByLanguage(language).stream().map(faqsEntity -> new FaqsModel(faqsEntity.getQuestion(),
                faqsEntity.getAnswer())).toList();
    }

    @Override
    public List<FaqsModel> getFaqsSubscription(String language, String transactionId) {
        return jsonLoaderPort.getJsonFaqsSubscriptions(language, transactionId);
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
    public List<ServersPromotions> getJsonServersPromoGuild(String language, String transactionId) {
        return jsonLoaderPort.getJsonServersPromoGuild(language, transactionId);
    }


    @Override
    public WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId) {
        return jsonLoaderPort.getWidgetSubscription(language, transactionId);
    }


    @Override
    public List<ExperiencesHomeModel> getExperiencesHome(String language, String transactionId) {
        return jsonLoaderPort.getExperiencesHome(language, transactionId);
    }

    @Override
    public List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId) {
        return jsonLoaderPort.getPlansAcquisition(language, transactionId);
    }

}
