package com.register.wowlibre.application.services.vote_wallet;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.port.in.user.*;
import com.register.wowlibre.domain.port.in.vote_wallet.*;
import com.register.wowlibre.domain.port.out.vote_wallet.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class VoteWalletService implements VoteWalletPort {
    private final ObtainVoteWallet obtainVoteWallet;
    private final SaveVoteWallet saveVoteWallet;
    private final UserPort userPort;

    public VoteWalletService(ObtainVoteWallet obtainVoteWallet, SaveVoteWallet saveVoteWallet, UserPort userPort) {
        this.obtainVoteWallet = obtainVoteWallet;
        this.saveVoteWallet = saveVoteWallet;
        this.userPort = userPort;
    }

    @Override
    public VoteWalletEntity findByReferenceCode(String referenceCode, String transactionId) {
        return obtainVoteWallet.findByReferenceCode(referenceCode, transactionId).orElseThrow(() ->
                new InternalException("Vote wallet not found for reference code: " + referenceCode, transactionId));
    }

    @Override
    public void saveVoteWallet(VoteWalletEntity voteWallet, String transactionId) {
        saveVoteWallet.saveVoteWallet(voteWallet, transactionId);
    }

    @Override
    public void create(Long userId, VotingPlatformsEntity platFormId, String referenceCode, String transactionId) {
        Optional<UserEntity> user = userPort.findByUserId(userId, transactionId);

        if (user.isEmpty()) {
            throw new InternalException("User with id " + userId + " does not exist.", transactionId);
        }

        VoteWalletEntity voteWallet = new VoteWalletEntity();
        voteWallet.setUserId(user.get());
        voteWallet.setPlatformId(platFormId);
        voteWallet.setReferenceCode(referenceCode);
        voteWallet.setTotalVotes(0);
        voteWallet.setVoteBalance(0);
        saveVoteWallet.saveVoteWallet(voteWallet, transactionId);

    }


    @Override
    public Optional<VoteWalletEntity> findByUserIdAndPlatformId(Long userId, Long platformId) {
        return obtainVoteWallet.findByUserIdAndPlatformId(userId, platformId);
    }
}
