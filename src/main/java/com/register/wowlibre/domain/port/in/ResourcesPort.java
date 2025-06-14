package com.register.wowlibre.domain.port.in;


import com.register.wowlibre.domain.dto.faqs.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.resources.*;

import java.util.*;

public interface ResourcesPort {
    List<CountryModel> getCountry(String transactionId);

    List<FaqsModel> getFaqs(FaqType type, String language, String transactionId);

    void createFaq(CreateFaqDto createFaq, String transactionId);

    void deleteFaq(Long faqId, String transactionId);

    List<PlanModel> getPlansBank(String language, String transactionId);

    List<BenefitModel> getBenefitsGuild(String language, String transactionId);

    List<ServersPromotions> getJsonServersPromoGuild(String language, String transactionId);

    WidgetHomeSubscriptionModel getWidgetSubscription(String language, String transactionId);

    List<PlanAcquisitionModel> getPlansAcquisition(String language, String transactionId);

}
