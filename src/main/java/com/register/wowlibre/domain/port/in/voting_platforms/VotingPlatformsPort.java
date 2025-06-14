package com.register.wowlibre.domain.port.in.voting_platforms;

import com.register.wowlibre.domain.model.*;

import java.util.*;

public interface VotingPlatformsPort {
    List<VotingPlatformsModel> findAllActiveVotingPlatforms(Long id, String transactionId);

    void createVotingPlatform(String name, String imgUrl, String postbackUrl, String allowedHost, String transactionId);

    void updateVotingPlatform(Long id, String name, String imgUrl, String postbackUrl, String allowedHost,
                              String transactionId);

    void deleteVotingPlatform(Long id, String transactionId);

    void postbackVotingPlatform(String referenceCode, String transactionId);
}
