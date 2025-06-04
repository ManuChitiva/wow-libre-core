package com.register.wowlibre.infrastructure.repositories.notification_providers;

import org.springframework.stereotype.*;

@Repository
public class JpaNotificationProvidersAdapter {
    private final NotificationProvidersRepository notificationProvidersRepository;

    public JpaNotificationProvidersAdapter(NotificationProvidersRepository notificationProvidersRepository) {
        this.notificationProvidersRepository = notificationProvidersRepository;
    }
}
