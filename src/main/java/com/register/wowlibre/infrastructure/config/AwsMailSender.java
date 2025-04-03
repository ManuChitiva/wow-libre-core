package com.register.wowlibre.infrastructure.config;

import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

@Component
public class AwsMailSender {

    private final AmazonSimpleEmailService sesClient;
    private final String fromEmail;
    private static final String TEMPLATES_PATH = "templates/";

    public AwsMailSender(AmazonSimpleEmailService sesClient,
                        @Value("${aws.ses.from-email}") String fromEmail) {
        this.sesClient = sesClient;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String to, String subject, String htmlBody, String transactionId) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message()
                    .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody)))
                    .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(fromEmail);

            sesClient.sendEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("Error sending email through AWS SES. Transaction ID: " + transactionId, e);
        }
    }

    public void sendTemplatedEmail(String to, String subject, String templateName,
                                   Map<String, String> templateVariables) {
        try {
            // Read the HTML template
            String htmlContent = loadTemplate(templateName);

            // Replace variables in template
            htmlContent = replaceTemplateVariables(htmlContent, templateVariables);

            // Create plain text version
            String plainText = convertHtmlToPlainText(htmlContent);

            // Create the email content
            Content htmlPart = new Content()
                    .withCharset("UTF-8")
                    .withData(htmlContent);

            Content textPart = new Content()
                    .withCharset("UTF-8")
                    .withData(plainText);

            Body body = new Body()
                    .withHtml(htmlPart)
                    .withText(textPart);

            Message message = new Message()
                    .withBody(body)
                    .withSubject(new Content()
                            .withCharset("UTF-8")
                            .withData(subject));

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(message)
                    .withSource(fromEmail);

            sesClient.sendEmail(request);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private String loadTemplate(String templateName) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(TEMPLATES_PATH + templateName)) {
            if (inputStream == null) {
                throw new IOException("Template not found: " + templateName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String replaceTemplateVariables(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String convertHtmlToPlainText(String html) {
        // Simple HTML to plain text conversion
        return html.replaceAll("<[^>]*>", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}