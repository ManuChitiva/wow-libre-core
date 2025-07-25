package com.register.wowlibre.application.services.mail;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.dto.comunication.*;
import com.register.wowlibre.domain.port.in.mail.*;
import com.register.wowlibre.infrastructure.config.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class MailService implements MailPort {

    private final AwsMailSender awsMailSender;
    private final Configurations configurations;
    private final MailSend mailSend;


    public MailService(Configurations configurations, AwsMailSender awsMailSender, MailSend mailSend) {
        this.configurations = configurations;
        this.awsMailSender = awsMailSender;
        this.mailSend = mailSend;
    }

    @Async
    @Override
    public void sendCodeMail(String email, String subject, String code, Locale locale, String transactionId) {
        final String url = String.format("%s/confirmation?email=%s&code=%s", configurations.getHostDomain(), email,
                code);

        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("url", url);

        //awsMailSender.sendTemplatedEmail(email, subject, "register.ftlh", templateVariables);
        mailSend.sendHTMLEmail(MailSenderVars.builder()
                .emailFrom(email).idTemplate(1)
                .data(RegisterSenderVarsDto.builder().url(url).build())
                .subject(subject).transactionId(transactionId).build());
    }

    @Override
    public void sendMail(String mail, String subject, String body, String transactionId) {
        //awsMailSender.sendEmail(mail, subject, body, transactionId);
        mailSend.sendMail(mail, subject, body, transactionId);
    }


}
