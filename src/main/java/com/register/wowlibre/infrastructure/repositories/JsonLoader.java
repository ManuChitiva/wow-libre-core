package com.register.wowlibre.infrastructure.repositories;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.out.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class JsonLoader implements JsonLoaderPort {
    private final ObjectMapper objectMapper;

    private final Resource jsonFile;
    private final Resource bankPlans;
    private final Resource benefitsGuild;
    private final Resource serversPromos;
    private final Resource bannersHome;
    private final Resource widgetHomeSubscription;
    private final Resource serverExperience;
    private final Resource plansAcquisition;

    private List<CountryModel> jsonCountryModel;
    private Map<String, List<PlanModel>> jsonPlanModel;
    private Map<String, List<BenefitModel>> jsonBenefits;
    private Map<String, List<ServersPromotions>> jsonServerPromos;
    private Map<String, List<BannerHomeModel>> jsonBannerHome;
    private Map<String, List<WidgetHomeSubscriptionModel>> jsonWidgetSubscription;
    private Map<String, List<ExperiencesHomeModel>> jsonServerExperienceModel;
    private Map<String, List<PlanAcquisitionModel>> jsonPlanAcquisitionModel;

    public JsonLoader(ObjectMapper objectMapper,
                      @Value("classpath:/static/countryAvailable.json") Resource jsonFile,
                      @Value("classpath:/static/bank_plans.json") Resource bankPlans,
                      @Value("classpath:/static/benefit_guild.json") Resource benefitsGuild,
                      @Value("classpath:/static/servers_promotions.json") Resource serverPromos,
                      @Value("classpath:/static/banner_home.json") Resource bannersHome,
                      @Value("classpath:/static/subscription_benefit.json") Resource widgetHomeSubscription,
                      @Value("classpath:/static/server_experiences.json") Resource serverExperience,
                      @Value("classpath:/static/plans_acquisition.json") Resource plansAcquisition) {
        this.objectMapper = objectMapper;
        this.jsonFile = jsonFile;
        this.bankPlans = bankPlans;
        this.benefitsGuild = benefitsGuild;
        this.serversPromos = serverPromos;
        this.bannersHome = bannersHome;
        this.widgetHomeSubscription = widgetHomeSubscription;
        this.serverExperience = serverExperience;
        this.plansAcquisition = plansAcquisition;
    }

    @PostConstruct
    public void loadJsonFile() {
        try {
            jsonBannerHome = readValue(bannersHome.getInputStream(), new TypeReference<>() {
            });
            jsonCountryModel = readValue(jsonFile.getInputStream(), new TypeReference<>() {
            });
            jsonPlanModel = readValue(bankPlans.getInputStream(), new TypeReference<>() {
            });
            jsonBenefits = readValue(benefitsGuild.getInputStream(), new TypeReference<>() {
            });
            jsonServerPromos = readValue(serversPromos.getInputStream(), new TypeReference<>() {
            });
            jsonWidgetSubscription = readValue(widgetHomeSubscription.getInputStream(), new TypeReference<>() {
            });
            jsonServerExperienceModel = readValue(serverExperience.getInputStream(), new TypeReference<>() {
            });
            jsonPlanAcquisitionModel = readValue(plansAcquisition.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(src, valueTypeRef);
    }

    @Override
    public List<BannerHomeModel> getBannersHome(String language, String transactionId) {
        return Optional.of(jsonBannerHome.get(language)).orElse(jsonBannerHome.get("es"));
    }

    @Override
    public List<CountryModel> getJsonCountry(String transactionId) {
        return jsonCountryModel;
    }


    @Override
    public List<PlanModel> getJsonPlans(String language, String transactionId) {
        return Optional.of(jsonPlanModel.get(language)).orElse(jsonPlanModel.get("es"));
    }

    @Override
    public List<BenefitModel> getJsonBenefitsGuild(String language, String transactionId) {
        return Optional.of(jsonBenefits.get(language)).orElse(jsonBenefits.get("es"));
    }

    @Override
    public List<ServersPromotions> getJsonServersPromoGuild(String language, String transactionId) {
        return Optional.of(jsonServerPromos.get(language)).orElse(jsonServerPromos.get("es"));
    }

    @Override
    public WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId) {
        return Optional.of(jsonWidgetSubscription.get(language).stream()
                .findFirst()).orElse(jsonWidgetSubscription.get("es").stream().findFirst()).orElse(null);
    }

    @Override
    public List<ExperiencesHomeModel> getExperiencesHome(String language, String transactionId) {
        return Optional.of(jsonServerExperienceModel.get(language)).orElse(jsonServerExperienceModel.get("es"));
    }

    @Override
    public List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId) {
        return Optional.of(jsonPlanAcquisitionModel.get(language)).orElse(jsonPlanAcquisitionModel.get("es"));
    }

}
