package com.imstargg.storage.db.core;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PlayerRenewalJpaRepository extends JpaRepository<PlayerRenewalEntity, Long> {

    Optional<PlayerRenewalEntity> findByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalEntity> findWithOptimisticLockByBrawlStarsTag(String brawlStarsTag);
}
