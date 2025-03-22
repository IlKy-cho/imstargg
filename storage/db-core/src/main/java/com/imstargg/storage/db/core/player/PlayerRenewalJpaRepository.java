package com.imstargg.storage.db.core.player;

import com.imstargg.core.enums.PlayerRenewalStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.Optional;

public interface PlayerRenewalJpaRepository extends JpaRepository<PlayerRenewalEntity, Long> {

    Optional<PlayerRenewalEntity> findByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalEntity> findVersionedByBrawlStarsTag(String brawlStarsTag);

    long countByStatusIn(Collection<PlayerRenewalStatus> statuses);
}
