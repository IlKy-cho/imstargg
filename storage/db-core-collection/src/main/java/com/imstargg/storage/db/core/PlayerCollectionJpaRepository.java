package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerCollectionJpaRepository extends JpaRepository<PlayerCollectionEntity, Long> {

    List<PlayerCollectionEntity> findAllByDeletedFalseAndStatusInOrderByNextUpdateTime(
            Collection<PlayerStatus> statuses, Limit limit);

    boolean existsByBrawlStarsTag(String brawlStarsTag);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<PlayerCollectionEntity> findWithOptimisticLockByBrawlStarsTag(String brawlStarsTag);
}
