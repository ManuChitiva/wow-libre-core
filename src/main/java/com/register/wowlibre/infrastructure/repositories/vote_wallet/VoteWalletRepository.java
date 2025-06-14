package com.register.wowlibre.infrastructure.repositories.vote_wallet;

import com.register.wowlibre.infrastructure.entities.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface VoteWalletRepository extends CrudRepository<VoteWalletEntity, Long> {
    Optional<VoteWalletEntity> findByReferenceCode(String referenceCode);
    Optional<VoteWalletEntity> findByUserId_IdAndPlatformId_Id(Long userId, Long platformId);
}
