package com.register.wowlibre.domain.port.out.voting_platforms;

import com.register.wowlibre.infrastructure.entities.*;

public interface SaveVotingPlatForms {
    void save(VotingPlatformsEntity entity);

    void deleteById(Long id);
}
