package com.register.wowlibre.domain.port.out;

import com.register.wowlibre.domain.model.resources.*;

import java.util.*;

public interface JsonLoaderPort {

    List<CountryModel> getJsonCountry(String transactionId);

    List<PlanModel> getJsonPlans(String language, String transactionId);

    List<BenefitModel> getJsonBenefitsGuild(String language, String transactionId);

    List<ServersPromotions> getJsonServersPromoGuild(String language, String transactionId);

    WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId);

    List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId);
}
