package com.register.wowlibre.domain.port.out.vote_wallet;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveVoteWallet {
    void saveVoteWallet(VoteWalletEntity voteWallet, String transactionId);
}
