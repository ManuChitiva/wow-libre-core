package com.register.wowlibre.application.services.mail;

import com.register.wowlibre.domain.port.in.mail.*;
import com.register.wowlibre.infrastructure.config.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class MailService implements MailPort {

    private final AwsMailSender awsMailSender;
    private final Configurations configurations;

    public MailService(Configurations configurations, AwsMailSender awsMailSender) {
        this.configurations = configurations;
        this.awsMailSender = awsMailSender;
    }

    @Async
    @Override
    public void sendCodeMail(String email, String subject, String code, Locale locale, String transactionId) {
        final String url = String.format("%s/confirmation?email=%s&code=%s", configurations.getHostDomain(), email,
                code);


        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("url", url);
        awsMailSender.sendTemplatedEmail(email, subject, "new-user.ftlh", templateVariables);
    }

    @Override
    public void sendMail(String mail, String subject, String body, String transactionId) {
        awsMailSender.sendEmail(mail, subject, body, transactionId);
    }


}
