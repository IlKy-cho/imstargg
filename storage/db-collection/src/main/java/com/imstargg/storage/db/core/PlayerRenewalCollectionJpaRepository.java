package com.imstargg.storage.db.core;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PlayerRenewalCollectionJpaRepository extends JpaRepository<PlayerRenewalCollectionEntity, Long> {

    Optional<PlayerRenewalCollectionEntity> findByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalCollectionEntity> findWithOptimisticLockById(Long id);
}
