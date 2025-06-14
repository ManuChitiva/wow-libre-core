package com.register.wowlibre.domain.port.out.voting_platforms;

import com.register.wowlibre.infrastructure.entities.*;

import java.util.*;

public interface ObtainVotingPlatforms {
    List<VotingPlatformsEntity> findAllActiveVotingPlatforms();
    Optional<VotingPlatformsEntity> findById(Long id);
}
