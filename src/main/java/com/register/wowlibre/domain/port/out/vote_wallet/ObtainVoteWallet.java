package com.register.wowlibre.domain.port.out.vote_wallet;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainVoteWallet {
    Optional<VoteWalletEntity> findByReferenceCode(String referenceCode, String transactionId);

    Optional<VoteWalletEntity> findByUserIdAndPlatformId(Long userId, Long platformId);
}
