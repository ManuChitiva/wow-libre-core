package com.register.wowlibre.domain.port.out.server_details;

import com.register.wowlibre.infrastructure.entities.*;

public interface DeleteServerDetails {
    void delete(RealmDetailsEntity detailsEntity, String transactionId);
}
