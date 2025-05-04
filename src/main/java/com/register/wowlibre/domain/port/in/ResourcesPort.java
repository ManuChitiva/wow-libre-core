package com.register.wowlibre.domain.port.in;


import com.register.wowlibre.domain.model.resources.*;

import java.util.*;

public interface ResourcesPort {
    List<BannerHomeModel> getBannersHome(String language, String transactionId);

    List<CountryModel> getCountry(String transactionId);

    List<FaqsModel> getFaqs(String language, String transactionId);

    List<FaqsModel> getFaqsSubscription(String language, String transactionId);

    List<PlanModel> getPlansBank(String language, String transactionId);

    List<BenefitModel> getBenefitsGuild(String language, String transactionId);

    List<ServersPromotions> getJsonServersPromoGuild(String language, String transactionId);

    WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId);

    List<ExperiencesHomeModel> getExperiencesHome(String language, String transactionId);

    List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId);

}
