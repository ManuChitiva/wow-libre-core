package com.register.wowlibre.domain.port.in.server_details;

import com.register.wowlibre.infrastructure.entities.*;

public interface DeleteServerDetailsPort {
    void delete(RealmDetailsEntity detailsEntity, String transactionId);
}
