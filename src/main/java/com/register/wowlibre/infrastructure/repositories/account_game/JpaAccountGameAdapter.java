package com.register.wowlibre.infrastructure.repositories.account_game;

import com.register.wowlibre.domain.port.out.account_game.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaAccountGameAdapter implements ObtainAccountGamePort, SaveAccountGamePort {
    private final AccountGameRepository accountGameRepository;

    public JpaAccountGameAdapter(AccountGameRepository accountGameRepository) {
        this.accountGameRepository = accountGameRepository;
    }

    @Override
    public List<AccountGameEntity> findByUserIdAndStatusIsTrue(Long userId, int page, int size, String transactionId) {
        return accountGameRepository.findByUserId_IdAndStatusIsTrue(userId, PageRequest.of(page, size)).stream().toList();
    }

    @Override
    public Optional<AccountGameEntity> findByUserIdAndAccountIdAndRealmIdAndStatusIsTrue(Long userId,
                                                                                         Long accountId,
                                                                                         Long realmId,
                                                                                         String transactionId) {
        return accountGameRepository.findByUserId_IdAndAccountIdAndRealmId_idAndStatusIsTrue(userId, accountId,
                realmId);
    }

    @Override
    public Long accounts(Long userId) {
        return accountGameRepository.countByUserId(userId);
    }

    @Override
    public List<AccountGameEntity> findByUserIdAndRealmNameAndUsernameStatusIsTrue(Long userId, int page, int size,
                                                                                   String realmName, String username,
                                                                                   String transactionId) {
        return accountGameRepository.findByUserId_IdAndStatusIsTrueAndRealmNameAndUsername(
                realmName, userId, username, PageRequest.of(page, size)).stream().toList();
    }

    @Override
    public List<AccountGameEntity> findByUserIdAndRealmId(Long userId, Long serverId, String transactionId) {
        return accountGameRepository.findByUserId_IdAndRealmId_IdAndStatusIsTrue(userId, serverId);
    }

    @Override
    public AccountGameEntity save(AccountGameEntity accountGameEntity, String transactionId) {
        return accountGameRepository.save(accountGameEntity);
    }
}
