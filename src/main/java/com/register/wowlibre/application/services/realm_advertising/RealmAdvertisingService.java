package com.register.wowlibre.application.services.realm_advertising;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.realm.*;
import com.register.wowlibre.domain.port.in.realm_advertising.*;
import com.register.wowlibre.domain.port.out.realm_advertising.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RealmAdvertisingService implements RealmAdvertisingPort {
    private final SaveRealmAdvertising saveRealmAdvertising;
    private final ObtainRealmAdvertising obtainRealmAdvertising;
    private final RealmPort realmPort;


    public RealmAdvertisingService(SaveRealmAdvertising saveRealmAdvertising,
                                   ObtainRealmAdvertising obtainRealmAdvertising, RealmPort realmPort) {
        this.saveRealmAdvertising = saveRealmAdvertising;
        this.obtainRealmAdvertising = obtainRealmAdvertising;
        this.realmPort = realmPort;
    }

    @Override
    public RealmAdvertisingModel getRealmAdvertisingById(Long realmId, String language, String transactionId) {
        Optional<RealmEntity> realm = realmPort.findById(realmId, transactionId);

        if (realm.isEmpty()) {
            throw new InternalException("Realm not found", transactionId);
        }

        return obtainRealmAdvertising.findByRealmId(realmId, language)
                .map(data -> RealmAdvertisingModel.builder()
                        .id(data.getId())
                        .title(data.getRealmId().getName())
                        .tag(data.getTag())
                        .subTitle(data.getSubTitle())
                        .description(data.getDescription())
                        .ctaPrimary(data.getCtaPrimary())
                        .imgUrl(data.getImgUrl())
                        .copySuccess(false)
                        .redirect(String.format("/vdp?id=%s&expansion=%s", data.getRealmId().getId(),
                                data.getRealmId().getExpansionId()))
                        .realmlist(data.getRealmId().getRealmlist())
                        .footerDisclaimer(data.getFooterDisclaimer())
                        .build())
                .orElseGet(() -> RealmAdvertisingModel.builder()
                        .title(realm.get().getName())
                        .redirect(String.format("/vdp?id=%s&expansion=%s", realm.get().getId(),
                                realm.get().getExpansionId()))
                        .copySuccess(false)
                        .realmlist(realm.get().getRealmlist())
                        .build());
    }

    @Override
    public void save(RealmAdvertisingDto realmAdvertisingModel, Long realmId, String transactionId) {

        Optional<RealmEntity> realm = realmPort.findById(realmId, transactionId);

        if (realm.isEmpty()) {
            throw new InternalException("Realm not found", transactionId);
        }

        Optional<RealmAdvertisingEntity> realmAdvertisingEntity =
                obtainRealmAdvertising.findByRealmId(realm.get().getId(), realmAdvertisingModel.getLanguage());

        RealmAdvertisingEntity realmAdvertising = realmAdvertisingEntity.orElseGet(RealmAdvertisingEntity::new);
        realmAdvertising.setRealmId(realm.get());
        realmAdvertising.setTag(realmAdvertisingModel.getTag());
        realmAdvertising.setSubTitle(realmAdvertisingModel.getSubTitle());
        realmAdvertising.setDescription(realmAdvertisingModel.getDescription());
        realmAdvertising.setCtaPrimary(realmAdvertisingModel.getCtaPrimary());
        realmAdvertising.setImgUrl(realmAdvertisingModel.getImgUrl());
        realmAdvertising.setFooterDisclaimer(realmAdvertisingModel.getFooterDisclaimer());
        realmAdvertising.setLanguage(realmAdvertisingModel.getLanguage());

        saveRealmAdvertising.save(realmAdvertising);
    }

    @Override
    public List<RealmAdvertisingModel> findByRealmsByLanguage(String language, String transactionId) {

        List<RealmAdvertisingEntity> realmAdvertisingList = obtainRealmAdvertising.findByLanguage(language,
                transactionId);

        return realmAdvertisingList.stream()
                .map(data -> RealmAdvertisingModel.builder()
                        .id(data.getId())
                        .title(data.getRealmId() != null ? data.getRealmId().getName() : null)
                        .tag(data.getTag())
                        .subTitle(data.getSubTitle())
                        .description(data.getDescription())
                        .ctaPrimary(data.getCtaPrimary())
                        .imgUrl(data.getImgUrl())
                        .copySuccess(false).redirect(String.format("/vdp?id=%s&expansion=%s", data.getRealmId().getId(),
                                data.getRealmId().getExpansionId()))
                        .realmlist(data.getRealmId() != null ? data.getRealmId().getRealmlist() : null)
                        .footerDisclaimer(data.getFooterDisclaimer())
                        .build())
                .toList();
    }
}
