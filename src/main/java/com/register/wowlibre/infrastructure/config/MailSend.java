package com.register.wowlibre.infrastructure.config;

import com.register.wowlibre.domain.dto.comunication.*;
import com.register.wowlibre.domain.enums.*;
import com.register.wowlibre.domain.exception.*;
import freemarker.template.*;
import jakarta.mail.internet.*;
import org.slf4j.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class MailSend {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSend.class);
    private final JavaMailSender mailSender;
    private final Configuration freeMakerConfiguration;

    public MailSend(JavaMailSender mailSender, Configuration freeMakerConfiguration) {
        this.mailSender = mailSender;
        this.freeMakerConfiguration = freeMakerConfiguration;
    }


    public void sendHTMLEmail(MailSenderVars<?> messageVars) {
        try {
            MimeMessage emailMessage = mailSender.createMimeMessage();

            MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true, "utf-8");
            String body = sendRegisterConfirmation(messageVars, template(messageVars.idTemplate));

            mailBuilder.setText(body, true);
            mailBuilder.setTo(messageVars.emailFrom);
            mailBuilder.setSubject(messageVars.subject);
            mailSender.send(emailMessage);
        } catch (Exception e) {
            LOGGER.error("It was not possible to send the communication message: [{}] Template [{}] ",
                    e.getMessage(), messageVars.idTemplate);
        }

    }

    public void sendMail(String email, String subject, String body, String transactionId) {
        try {
            MimeMessage emailMessage = mailSender.createMimeMessage();

            MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true, "UTF-8");

            mailBuilder.setText(body, false);
            mailBuilder.setTo(email);
            mailBuilder.setSubject(subject);

            mailSender.send(emailMessage);
        } catch (Exception e) {
            LOGGER.error("It was not possible to send the communication message: [{}] transactionId {}",
                    e.getMessage(), transactionId);
            throw new InternalException("It was not possible to send the communication message", transactionId);
        }
    }


    private String sendRegisterConfirmation(MailSenderVars<?> body, String template) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("body", body.data);

        buildBody(model, stringWriter, template);
        return stringWriter.getBuffer().toString();
    }

    private void buildBody(Map<String, Object> model, StringWriter stringWriter, String template) {
        try {
            freeMakerConfiguration.getTemplate(template).process(model, stringWriter);
        } catch (Exception e) {
            LOGGER.error("It was not possible to load the template: [{}] Message: [{}]", template,
                    e.getMessage());
        }
    }

    private String template(Integer idTemplate) {
        return EmailTemplate.getTemplateNameById(idTemplate);
    }
}
