package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.shared.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.register.wowlibre.domain.constant.Constants.*;

@RestController
@RequestMapping("/api/resources")
public class ResourcesController {

    private final ResourcesPort resourcesPort;

    public ResourcesController(ResourcesPort resourcesPort) {
        this.resourcesPort = resourcesPort;
    }

    @GetMapping("/country")
    public ResponseEntity<GenericResponse<List<CountryModel>>> country(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId) {
        final List<CountryModel> countryModelList = resourcesPort.getCountry(transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(countryModelList, transactionId).ok().build());
    }

    @GetMapping("/faqs")
    public ResponseEntity<GenericResponse<List<FaqsModel>>> faqs(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE, required = false) Locale locale) {
        final List<FaqsModel> faqs = resourcesPort.getFaqs(locale.getLanguage(), transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(faqs, transactionId).ok().build());
    }


    @GetMapping("/faqs-subscriptions")
    public ResponseEntity<GenericResponse<List<FaqsModel>>> faqsSubscriptions(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE, required = false) Locale locale) {
        final List<FaqsModel> faqsModels = resourcesPort.getFaqsSubscription(locale.getLanguage(), transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(faqsModels, transactionId).ok().build());
    }

    @GetMapping("/experiences")
    public ResponseEntity<GenericResponse<List<ExperiencesHomeModel>>> experiences(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE, required = false) Locale locale) {
        final List<ExperiencesHomeModel> faqsModels = resourcesPort.getExperiencesHome(locale.getLanguage(),
                transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(faqsModels, transactionId).ok().build());
    }

    @GetMapping("/bank/plans")
    public ResponseEntity<GenericResponse<List<PlanModel>>> plans(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        final List<PlanModel> countryModelList = resourcesPort.getPlansBank(locale.getLanguage(), transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(countryModelList, transactionId).ok().build());
    }

    @GetMapping("/benefits-guild")
    public ResponseEntity<GenericResponse<List<BenefitModel>>> benefitsGuild(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {
        final List<BenefitModel> benefitsGuild = resourcesPort.getBenefitsGuild(locale.getLanguage(), transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(benefitsGuild, transactionId).ok().build());
    }


    @GetMapping("/server-promos")
    public ResponseEntity<GenericResponse<List<ServersPromotions>>> serversPromo(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {
        final List<ServersPromotions> serversPromotions = resourcesPort.getJsonServersPromoGuild(locale.getLanguage()
                , transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(serversPromotions, transactionId).ok().build());
    }

    @GetMapping("/banners-home")
    public ResponseEntity<GenericResponse<List<BannerHomeModel>>> bannersHome(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        final List<BannerHomeModel> serversPromotions = resourcesPort.getBannersHome(locale.getLanguage(),
                transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(serversPromotions, transactionId).ok().build());
    }

    @GetMapping("/widget-home")
    public ResponseEntity<GenericResponse<WidgetHomeSubscriptionModel>> widgetSubscription(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        final WidgetHomeSubscriptionModel serversPromotions =
                resourcesPort.getWidgetSubscription(locale.getLanguage(),
                        transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(serversPromotions, transactionId).ok().build());
    }


    @GetMapping("/plan-acquisition")
    public ResponseEntity<GenericResponse<List<PlanAcquisitionModel>>> planAcquisition(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale) {

        final List<PlanAcquisitionModel> benefitsGuild = resourcesPort.getPlansAcquisition(locale.getLanguage(),
                transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(benefitsGuild, transactionId).ok().build());
    }
}
