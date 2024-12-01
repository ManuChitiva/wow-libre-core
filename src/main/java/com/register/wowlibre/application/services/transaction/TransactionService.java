package com.register.wowlibre.application.services.transaction;

import com.register.wowlibre.domain.dto.*;
import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.*;
import com.register.wowlibre.domain.port.in.account_game.*;
import com.register.wowlibre.domain.port.in.integrator.*;
import com.register.wowlibre.domain.port.in.transaction.*;
import com.register.wowlibre.domain.port.in.user_promotion.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TransactionService implements TransactionPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final IntegratorPort integratorPort;
    private final AccountGamePort accountGamePort;
    private final ResourcesPort resourcesPort;
    private final UserPromotionPort userPromotionPort;

    public TransactionService(IntegratorPort integratorPort, AccountGamePort accountGamePort,
                              ResourcesPort resourcesPort, UserPromotionPort userPromotionPort) {
        this.integratorPort = integratorPort;
        this.accountGamePort = accountGamePort;
        this.resourcesPort = resourcesPort;
        this.userPromotionPort = userPromotionPort;
    }


    @Override
    public void purchase(Long serverId, Long userId, Long accountId, String reference, List<ItemQuantityModel> items,
                         Double amount, String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        ServerEntity server = accountVerificationDto.server();

        if (server == null) {
            LOGGER.error("Server is not available");
            throw new InternalException("Server is not available", transactionId);
        }

        integratorPort.purchase(server.getIp(), server.getJwt(), userId, accountId, reference, items, amount,
                transactionId);
    }

    @Override
    public void sendSubscriptionBenefits(Long serverId, Long userId, Long accountId, Long characterId,
                                         List<ItemQuantityModel> items, String benefitType, Double amount,
                                         String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        ServerEntity server = accountVerificationDto.server();

        if (server == null) {
            LOGGER.error("Server is not available");
            throw new InternalException("Server is not available", transactionId);
        }

        integratorPort.sendBenefitsPremium(server.getIp(), server.getJwt(), userId, accountId, characterId, items,
                benefitType, amount, transactionId);

    }

    @Override
    public PromotionsDto getPromotions(Long serverId, Long userId, Long accountId, Long characterId, String language,
                                       String transactionId) {

        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        ServerEntity server = accountVerificationDto.server();

        if (server == null) {
            LOGGER.error("Server is not available");
            throw new InternalException("Server is not available", transactionId);
        }

        List<PromotionModel> promotions =
                resourcesPort.getPromotions(language, transactionId).stream()
                        .filter(promos -> Objects.equals(promos.getServerId(), serverId)).toList();

        if (promotions.isEmpty()) {
            return new PromotionsDto(new ArrayList<>(), 0);
        }

        return new PromotionsDto(promotions.stream()
                .filter(promoValidation ->
                        userPromotionPort.findByUserIdAndAccountId(
                                userId, accountId, promoValidation.getId(), characterId, transactionId).isEmpty())
                .map(PromotionDto::new).toList(), promotions.size());
    }

    @Override
    public void claimPromotion(Long serverId, Long userId, Long accountId, Long characterId, Long promotionId,
                               String language, String transactionId) {

        if (userPromotionPort.findByUserIdAndAccountId(
                userId, accountId, promotionId, characterId, transactionId).isPresent()) {
            throw new InternalException("You have already consumed the promotion", transactionId);
        }


        AccountVerificationDto accountVerificationDto = accountGamePort.verifyAccount(userId, accountId, serverId,
                transactionId);

        ServerEntity server = accountVerificationDto.server();

        if (server == null) {
            LOGGER.error("Server is not available");
            throw new InternalException("Server is not available", transactionId);
        }

        Optional<PromotionModel> promotions =
                resourcesPort.getPromotions(language, transactionId).stream()
                        .filter(promos -> Objects.equals(promos.getServerId(), serverId)
                                && promos.getId().equals(promotionId)).findFirst();

        if (promotions.isEmpty()) {
            throw new InternalException("The promotion is not available, contact support", transactionId);
        }

        PromotionModel promo = promotions.get();
        List<ItemQuantityModel> items = new ArrayList<>();

        if (promo.getSendItem() && !promo.getItems().isEmpty()) {
            items = promo.getItems().stream().map(benefit -> new ItemQuantityModel(benefit.code,
                    benefit.quantity)).toList();
        }

        integratorPort.sendPromo(server.getIp(), server.getJwt(), userId, accountId, characterId, items,
                promo.getType(), promo.getAmount(), promo.getMinLvl(), promo.getMaxLvl(),
                transactionId);

        userPromotionPort.save(userId, accountId, promotionId, transactionId);
    }
}
