package com.imstargg.storage.db.core.player;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UnknownPlayerCollectionJpaRepository extends JpaRepository<UnknownPlayerCollectionEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<UnknownPlayerCollectionEntity> findVersionedByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<UnknownPlayerCollectionEntity> findOptimisticLockByBrawlStarsTag(String brawlStarsTag);

    boolean existsByBrawlStarsTag(String brawlStarsTag);
}
