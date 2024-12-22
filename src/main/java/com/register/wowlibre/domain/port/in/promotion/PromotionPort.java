package com.register.wowlibre.domain.port.in.promotion;

import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface PromotionPort {
    List<PromotionModel> findByPromotionServerIdAndLanguage(Long serverId, Long classId, String language,
                                                            String transactionId);
}
