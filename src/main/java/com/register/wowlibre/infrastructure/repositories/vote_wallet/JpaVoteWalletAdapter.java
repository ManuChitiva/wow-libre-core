package com.register.wowlibre.infrastructure.repositories.vote_wallet;

import com.register.wowlibre.domain.port.out.vote_wallet.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaVoteWalletAdapter implements SaveVoteWallet, ObtainVoteWallet {
    private final VoteWalletRepository voteWalletRepository;

    public JpaVoteWalletAdapter(VoteWalletRepository voteWalletRepository) {
        this.voteWalletRepository = voteWalletRepository;
    }

    @Override
    public Optional<VoteWalletEntity> findByReferenceCode(String referenceCode, String transactionId) {
        return voteWalletRepository.findByReferenceCode(referenceCode);
    }

    @Override
    public Optional<VoteWalletEntity> findByUserIdAndPlatformId(Long userId, Long platformId) {
        return voteWalletRepository.findByUserId_IdAndPlatformId_Id(userId, platformId);
    }

    @Override
    public void saveVoteWallet(VoteWalletEntity voteWallet, String transactionId) {
        voteWalletRepository.save(voteWallet);
    }
}
