package com.register.wowlibre.application.services.voting_platforms;

import com.register.wowlibre.domain.exception.*;
import com.register.wowlibre.domain.model.*;
import com.register.wowlibre.domain.port.in.vote_wallet.*;
import com.register.wowlibre.domain.port.in.voting_platforms.*;
import com.register.wowlibre.domain.port.out.voting_platforms.*;
import com.register.wowlibre.infrastructure.entities.*;
import com.register.wowlibre.infrastructure.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class VotingPlatformsService implements VotingPlatformsPort {

    private final ObtainVotingPlatforms obtainVotingPlatforms;
    private final SaveVotingPlatForms saveVotingPlatformPort;
    private final VoteWalletPort voteWalletPort;
    private final RandomString randomString;

    public VotingPlatformsService(ObtainVotingPlatforms obtainVotingPlatforms,
                                  SaveVotingPlatForms saveVotingPlatformPort, VoteWalletPort voteWalletPort,
                                  @Qualifier("reset-password-string") RandomString randomString) {
        this.obtainVotingPlatforms = obtainVotingPlatforms;
        this.saveVotingPlatformPort = saveVotingPlatformPort;
        this.voteWalletPort = voteWalletPort;
        this.randomString = randomString;
    }

    @Override
    public List<VotingPlatformsModel> findAllActiveVotingPlatforms(Long userId, String transactionId) {
        return obtainVotingPlatforms.findAllActiveVotingPlatforms().stream().map(data -> mapToModel(userId, data,
                transactionId)).toList();
    }

    @Override
    public void createVotingPlatform(String name, String imgUrl, String postbackUrl, String allowedHost,
                                     String transactionId) {
        VotingPlatformsEntity entity = new VotingPlatformsEntity();
        entity.setName(name);
        entity.setImgUrl(imgUrl);
        entity.setPostbackUrl(postbackUrl);
        entity.setAllowedHost(allowedHost);
        entity.setActive(true);
        saveVotingPlatformPort.save(entity);
    }

    @Override
    public void updateVotingPlatform(Long id, String name, String imgUrl, String postbackUrl, String allowedHost,
                                     String transactionId) {
        Optional<VotingPlatformsEntity> optional = obtainVotingPlatforms.findById(id);

        if (optional.isEmpty()) {
            throw new InternalException("Voting platform with id " + id + " does not exist.", transactionId);
        }

        VotingPlatformsEntity entity = optional.get();
        entity.setName(name);
        entity.setImgUrl(imgUrl);
        entity.setPostbackUrl(postbackUrl);
        entity.setAllowedHost(allowedHost);
        saveVotingPlatformPort.save(entity);
    }

    @Override
    public void deleteVotingPlatform(Long id, String transactionId) {
        saveVotingPlatformPort.deleteById(id);
    }

    @Override
    public void postbackVotingPlatform(String referenceCode, String transactionId) {
        VoteWalletEntity walletVote = voteWalletPort.findByReferenceCode(referenceCode, transactionId);
        walletVote.setTotalVotes(walletVote.getVoteBalance() + 1);
        walletVote.setUpdatedAt(LocalDateTime.now());
        walletVote.setVoteBalance(walletVote.getVoteBalance() + 1);
        voteWalletPort.saveVoteWallet(walletVote, transactionId);
    }

    private VotingPlatformsModel mapToModel(Long userId, VotingPlatformsEntity entity, String transactionId) {

        if (userId == null) {
            return new VotingPlatformsModel(
                    entity.getId(),
                    entity.getImgUrl(),
                    entity.getName(),
                    null
            );
        }

        Optional<VoteWalletEntity> walletPlatform = voteWalletPort.findByUserIdAndPlatformId(userId, entity.getId());
        String postbackUrl = null;
        String referenceCode;

        if (walletPlatform.isEmpty()) {
            referenceCode = randomString.nextString();
            voteWalletPort.create(userId, entity, referenceCode, transactionId);
        } else {
            referenceCode = walletPlatform.get().getReferenceCode();
        }

        if (entity.getPostbackUrl() != null && referenceCode != null) {
            String replacement = referenceCode + "-" + entity.getId();
            postbackUrl = entity.getPostbackUrl().replace("changeme", replacement);
        }

        return new VotingPlatformsModel(
                entity.getId(),
                entity.getImgUrl(),
                entity.getName(),
                postbackUrl
        );
    }
}
