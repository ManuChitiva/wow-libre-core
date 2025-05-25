package com.register.wowlibre.domain.port.in.realm_advertising;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface RealmAdvertisingPort {
    RealmAdvertisingModel getRealmAdvertisingById(Long realmId, String language, String transactionId);

    void save(RealmAdvertisingDto realmAdvertisingDto, Long realmId, String transactionId);

    List<RealmAdvertisingModel> findByRealmsByLanguage(String language, String transactionId);
}
