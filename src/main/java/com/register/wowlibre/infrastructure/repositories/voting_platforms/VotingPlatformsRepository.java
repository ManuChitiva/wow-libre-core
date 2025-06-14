package com.register.wowlibre.infrastructure.repositories.voting_platforms;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface VotingPlatformsRepository extends CrudRepository<VotingPlatformsEntity, Long> {
    List<VotingPlatformsEntity> findAllByIsActiveTrue();
}
