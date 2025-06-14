package com.register.wowlibre.infrastructure.repositories.voting_platforms;

import com.register.wowlibre.domain.port.out.voting_platforms.*;
import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaVotingPlatformsAdapter implements ObtainVotingPlatforms, SaveVotingPlatForms {
    private final VotingPlatformsRepository votingPlatformsRepository;

    public JpaVotingPlatformsAdapter(VotingPlatformsRepository votingPlatformsRepository) {
        this.votingPlatformsRepository = votingPlatformsRepository;
    }

    @Override
    public List<VotingPlatformsEntity> findAllActiveVotingPlatforms() {
        return votingPlatformsRepository.findAllByIsActiveTrue();
    }

    @Override
    public Optional<VotingPlatformsEntity> findById(Long id) {
        return votingPlatformsRepository.findById(id);
    }

    @Override
    public void save(VotingPlatformsEntity entity) {
        votingPlatformsRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        votingPlatformsRepository.deleteById(id);
    }
}
