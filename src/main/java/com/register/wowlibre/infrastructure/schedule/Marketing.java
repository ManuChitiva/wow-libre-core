package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.infrastructure.config.*;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class Marketing {
    private static final Logger LOGGER = LoggerFactory.getLogger(Marketing.class);
    private final UserPort userPort;
    private final AwsMailSender awsMailSender;


    public Marketing(UserPort userPort, AwsMailSender awsMailSender) {
        this.userPort = userPort;
        this.awsMailSender = awsMailSender;
    }


    @Scheduled(cron = "1 0/1 * * * *")
    public void sendMailsMarketing() {
        userPort.findAll("Marketing").forEach(user -> {
          try{
              awsMailSender.sendTemplatedEmail(user.getEmail(), "¡Te extrañamos! Vuelve y descubre lo que tenemos para " +
                      "ti", "casino.html", new HashMap<>());
              Thread.sleep(1000);
          }catch (Exception e){
              LOGGER.error("Error al enviar el correo: {}", e.getMessage());
          }
        });
    }
}
