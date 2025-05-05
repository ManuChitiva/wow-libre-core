package com.register.wowlibre.infrastructure.config;

import com.register.wowlibre.infrastructure.util.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
public class ConfigRandomSerial {
    @Bean("reset-password-string")
    public RandomString configRandomString() {
        return new RandomString(15, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("reference-serial-bank")
    public RandomString referenceSerial() {
        return new RandomString(20, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("random-code")
    public RandomString randomCodeString() {
        return new RandomString(28, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("random-send-otp")
    public RandomString configRandomOtpString() {
        return new RandomString(5, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

}
