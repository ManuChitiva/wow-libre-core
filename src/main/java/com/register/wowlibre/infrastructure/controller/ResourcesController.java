package com.register.wowlibre.infrastructure.controller;

import com.register.wowlibre.domain.dto.faqs.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.shared.*;
import jakarta.validation.*;
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
            @RequestHeader(name = HEADER_ACCEPT_LANGUAGE) Locale locale,
            @RequestParam(name = "type") String type) {

        FaqType faqType = FaqType.getByName(type);

        final List<FaqsModel> faqs = resourcesPort.getFaqs(faqType, locale.getLanguage(), transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<>(faqs, transactionId).ok().build());
    }

    @PostMapping("/create/faq")
    public ResponseEntity<GenericResponse<Void>> createFaq(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestBody @Valid CreateFaqDto createFaqDto) {

        resourcesPort.createFaq(createFaqDto, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
    }

    @DeleteMapping("/delete/faq")
    public ResponseEntity<GenericResponse<Void>> deleteFaq(
            @RequestHeader(name = HEADER_TRANSACTION_ID, required = false) final String transactionId,
            @RequestParam Long id) {

        resourcesPort.deleteFaq(id, transactionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponseBuilder<Void>(transactionId).ok().build());
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
