package com.imstargg.storage.db.core.player;

import com.imstargg.core.enums.PlayerRenewalStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerRenewalCollectionJpaRepository extends JpaRepository<PlayerRenewalCollectionEntity, Long> {

    Optional<PlayerRenewalCollectionEntity> findByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalCollectionEntity> findVersionedByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalCollectionEntity> findWithOptimisticLockById(Long id);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerRenewalCollectionEntity> findWithOptimisticLockByBrawlStarsTag(String brawlStarsTag);

    List<PlayerRenewalCollectionEntity> findAllByStatusIn(Collection<PlayerRenewalStatus> statusList);
}
