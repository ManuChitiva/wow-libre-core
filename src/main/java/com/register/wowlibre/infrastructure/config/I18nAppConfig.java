package com.register.wowlibre.infrastructure.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.i18n.*;

import java.util.*;

@Configuration
public class I18nAppConfig {
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);

        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
}
