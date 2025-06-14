package com.register.wowlibre.domain.port.in.vote_wallet;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface VoteWalletPort {
    VoteWalletEntity findByReferenceCode(String referenceCode, String transactionId);

    void saveVoteWallet(VoteWalletEntity voteWallet, String transactionId);

    void create(Long userId, VotingPlatformsEntity platFormId, String referenceCode, String transactionId);

    Optional<VoteWalletEntity> findByUserIdAndPlatformId(Long userId, Long platformId);
}
