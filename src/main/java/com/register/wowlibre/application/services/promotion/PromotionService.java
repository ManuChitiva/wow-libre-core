package com.register.wowlibre.application.services.promotion;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.promotion.*;
import com.register.wowlibre.domain.port.out.promotion.*;
import com.register.wowlibre.domain.port.out.promotion_item.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PromotionService implements PromotionPort {
    private final ObtainPromotion obtainPromotion;
    private final ObtainPromotionItem obtainPromotionItem;

    public PromotionService(ObtainPromotion obtainPromotion, ObtainPromotionItem obtainPromotionItem) {
        this.obtainPromotion = obtainPromotion;
        this.obtainPromotionItem = obtainPromotionItem;
    }


    @Override
    public List<PromotionModel> findByPromotionServerIdAndClassIdAndLanguage(Long serverId, Long classId,
                                                                             String language, String transactionId) {
        List<PromotionModel> promotions = new ArrayList<>();

        List<PromotionEntity> promotionsDb = obtainPromotion.findByPromotionServerIdAndLanguage(serverId, language);

        if (promotionsDb.isEmpty()) {
            return promotions;
        }

        List<PromotionEntity> filterClassPromotional =
                promotionsDb.stream()
                        .filter(promo -> promo.getClassCharacter() == null
                                || promo.getClassCharacter() == 0
                                || promo.getClassCharacter().equals(classId)
                        ).toList();

        if (filterClassPromotional.isEmpty()) {
            return promotions;
        }

        promotionsBuild(transactionId, filterClassPromotional, promotions);

        return promotions;
    }

    @Override
    public PromotionModel findByPromotionServerIdAndLanguage(Long id, Long serverId, String language,
                                                             String transactionId) {


        Optional<PromotionEntity> promotionsDb = obtainPromotion.findByIdAndServerIdAndLanguage(id, serverId, language);

        if (promotionsDb.isEmpty()) {
            throw new InternalException("The promotion is not found or is not available", transactionId);
        }

        PromotionEntity promotionEntity = promotionsDb.get();

        List<PromotionItemEntity> promotionItem = obtainPromotionItem.findByPromotionId(promotionEntity,
                transactionId);


        return PromotionModel.builder()
                .id(promotionEntity.getId())
                .amount(promotionEntity.getAmount())
                .reference(promotionEntity.getReference())
                .img(promotionEntity.getImg())
                .name(promotionEntity.getName())
                .description(promotionEntity.getDescription())
                .btnTxt(promotionEntity.getBtnText())
                .sendItem(promotionEntity.isSendItem())
                .type(promotionEntity.getType())
                .minLvl(promotionEntity.getMinLevel())
                .maxLvl(promotionEntity.getMaxLevel())
                .level(promotionEntity.getLevel())
                .status(promotionEntity.isStatus())
                .serverId(promotionEntity.getServerId())
                .items(promotionItem.stream().map(this::promotionItemMapToModel).toList())
                .classId(promotionEntity.getClassCharacter())
                .build();
    }

    @Override
    public List<PromotionModel> findByPromotionServerId(Long serverId, String transactionId) {
        List<PromotionModel> promotions = new ArrayList<>();

        List<PromotionEntity> promotionDb = obtainPromotion.findByPromotionServerId(serverId);

        promotionsBuild(transactionId, promotionDb, promotions);

        return promotions;
    }

    private void promotionsBuild(String transactionId, List<PromotionEntity> promotionDb, List<PromotionModel> promotions) {
        for (PromotionEntity promotionEntity : promotionDb) {
            List<PromotionItemEntity> promotionItem = obtainPromotionItem.findByPromotionId(promotionEntity,
                    transactionId);

            promotions.add(PromotionModel.builder()
                    .id(promotionEntity.getId())
                    .amount(promotionEntity.getAmount())
                    .reference(promotionEntity.getReference())
                    .img(promotionEntity.getImg())
                    .name(promotionEntity.getName())
                    .description(promotionEntity.getDescription())
                    .btnTxt(promotionEntity.getBtnText())
                    .sendItem(promotionEntity.isSendItem())
                    .level(promotionEntity.getLevel())
                    .status(promotionEntity.isStatus())
                    .type(promotionEntity.getType())
                    .minLvl(promotionEntity.getMinLevel())
                    .maxLvl(promotionEntity.getMaxLevel())
                    .serverId(promotionEntity.getServerId())
                    .items(promotionItem.stream().map(this::promotionItemMapToModel).toList())
                    .classId(promotionEntity.getClassCharacter())
                    .build());
        }
    }

    private PromotionModel.Items promotionItemMapToModel(PromotionItemEntity promotionItem) {
        return new PromotionModel.Items(promotionItem.getCode(), promotionItem.getQuantity());
    }
}
