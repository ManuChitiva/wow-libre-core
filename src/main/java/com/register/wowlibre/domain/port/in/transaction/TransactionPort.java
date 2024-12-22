package com.register.wowlibre.domain.port.in.transaction;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface TransactionPort {
    void purchase(Long serverId, Long userId, Long accountId, String reference, List<ItemQuantityModel> items,
                  Double amount, String transactionId);

    void sendSubscriptionBenefits(Long serverId, Long userId, Long accountId, Long characterId,
                                  List<ItemQuantityModel> items, String benefitType, Double amount,
                                  String transactionId);

    PromotionsDto getPromotions(Long serverId, Long userId, Long accountId, Long characterId, Long classId,
                                String language,
                                String transactionId);

    void claimPromotion(Long serverId, Long userId, Long accountId, Long characterId, Long promotionId, String language,
                        String transactionId);
}
