package com.register.wowlibre.controller;

import com.register.wowlibre.domain.dto.faqs.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.model.resources.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.shared.*;
import com.register.wowlibre.infrastructure.controller.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResourcesControllerTest {

    @Mock
    private ResourcesPort resourcesPort;

    private ResourcesController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ResourcesController(resourcesPort);
    }

    @Test
    void testGetCountries() {
        List<CountryModel> mockCountries = List.of(new CountryModel("PE", "PE", "ES", "Peru"));
        when(resourcesPort.getCountry("tx123")).thenReturn(mockCountries);

        ResponseEntity<GenericResponse<List<CountryModel>>> response =
                controller.country("tx123");

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(mockCountries, response.getBody().getData());
    }

    @Test
    void testGetFaqs() {
        List<FaqsModel> mockFaqs = List.of(new FaqsModel(1L, "title", "desc", FaqType.SUPPORT));
        when(resourcesPort.getFaqs(FaqType.SUPPORT, "es", "tx456")).thenReturn(mockFaqs);

        ResponseEntity<GenericResponse<List<FaqsModel>>> response =
                controller.faqs("tx456", Locale.forLanguageTag("es"), FaqType.SUPPORT.name());

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(mockFaqs, response.getBody().getData());
    }

    @Test
    void testCreateFaq() {
        CreateFaqDto dto = new CreateFaqDto();
        dto.setAnswer("Answer content");
        dto.setLanguage("es");
        dto.setQuestion("What is this?");

        doNothing().when(resourcesPort).createFaq(dto, "tx789");

        ResponseEntity<GenericResponse<Void>> response =
                controller.createFaq("tx789", dto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testDeleteFaq() {
        doNothing().when(resourcesPort).deleteFaq(42L, "tx321");

        ResponseEntity<GenericResponse<Void>> response =
                controller.deleteFaq("tx321", 42L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testGetPlansBank() {
        List<PlanModel> plans = List.of(new PlanModel(1L, "PlanGold", "Description", "12.99", "USD", new ArrayList<>(),
                "https://example.com/image.png", 1, 2, 2d));
        when(resourcesPort.getPlansBank("en", "tx999")).thenReturn(plans);

        ResponseEntity<GenericResponse<List<PlanModel>>> response =
                controller.plans("tx999", Locale.ENGLISH);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(plans, response.getBody().getData());
    }

    @Test
    void testGetBenefitsGuild() {
        List<BenefitModel> benefits = List.of(new BenefitModel(1L, "Benefit A", "Description A", "es", "https" +
                "://example.com/benefit.png", "item1", 1, true, "link.com"));
        when(resourcesPort.getBenefitsGuild("es", "tx111")).thenReturn(benefits);

        ResponseEntity<GenericResponse<List<BenefitModel>>> response =
                controller.benefitsGuild("tx111", Locale.forLanguageTag("es"));

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(benefits, response.getBody().getData());
    }

    @Test
    void testWidgetSubscription() {
        WidgetHomeSubscriptionModel widget = new WidgetHomeSubscriptionModel();
        widget.title = "Widget Title";
        widget.description = "Widget Description";
        widget.btn = "Subscribe Now";

        when(resourcesPort.getWidgetSubscription("en", "tx222")).thenReturn(widget);

        ResponseEntity<GenericResponse<WidgetHomeSubscriptionModel>> response =
                controller.widgetSubscription("tx222", Locale.ENGLISH);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(widget, response.getBody().getData());
    }

    @Test
    void testPlanAcquisition() {
        List<PlanAcquisitionModel> acquisitions = List.of(new PlanAcquisitionModel("Plan A", "123", "USD", List.of(),
                "Subscribite", "https://example.com/plan.png"));
        when(resourcesPort.getPlansAcquisition("en", "tx333")).thenReturn(acquisitions);

        ResponseEntity<GenericResponse<List<PlanAcquisitionModel>>> response =
                controller.planAcquisition("tx333", Locale.ENGLISH);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(acquisitions, response.getBody().getData());
    }
}
