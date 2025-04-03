package com.register.wowlibre.infrastructure.schedule;

import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.infrastructure.config.*;
import jakarta.annotation.*;
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

    @PostConstruct
    public void init() {
        sendMailsMarketing();
    }


    @Scheduled(cron = "0 0 0 1/30 * ?") // Se ejecuta cada 30 días a la medianoche
    public void sendMailsMarketing() {

    }
}
