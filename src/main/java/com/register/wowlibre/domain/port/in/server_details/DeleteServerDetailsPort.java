package com.register.wowlibre.domain.port.in.server_details;

import com.register.wowlibre.infrastructure.entities.*;

public interface DeleteServerDetailsPort {
    void delete(ServerDetailsEntity detailsEntity, String transactionId);
}
