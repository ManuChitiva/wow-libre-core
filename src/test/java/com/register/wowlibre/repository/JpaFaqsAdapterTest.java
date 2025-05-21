package com.register.wowlibre.repository;

import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.repositories.faqs.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaFaqsAdapterTest {
    @Mock
    private FaqsRepository faqsRepository;

    private JpaFaqsAdapter jpaFaqsAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jpaFaqsAdapter = new JpaFaqsAdapter(faqsRepository);
    }

    @Test
    void testFindByLanguage_returnsFaqList() {
        // Arrange
        String language = "es";
        List<FaqsEntity> expectedFaqs = List.of(
                createFaqEntity(1L, "¿Qué es?", "Una prueba.", "en"),
                createFaqEntity(2L, "¿Cómo funciona?", "Con magia.", "es")
        );

        when(faqsRepository.findByTypeAndLanguage(FaqType.SUPPORT, language)).thenReturn(expectedFaqs);

        // Act
        List<FaqsEntity> actualFaqs = jpaFaqsAdapter.findByTypeAndLanguage(FaqType.SUPPORT, language);

        // Assert
        assertEquals(expectedFaqs.size(), actualFaqs.size());
        assertEquals(expectedFaqs, actualFaqs);
        verify(faqsRepository, times(1)).findByTypeAndLanguage(FaqType.SUPPORT, language);
    }

    private FaqsEntity createFaqEntity(Long id, String question, String answer, String language) {
        FaqsEntity faq = new FaqsEntity();
        faq.setId(id);
        faq.setQuestion(question);
        faq.setAnswer(answer);
        faq.setLanguage(language);
        return faq;
    }
}
