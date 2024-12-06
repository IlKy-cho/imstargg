package com.imstargg.storage.db.core;

import com.imstargg.core.enums.UnknownPlayerStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UnknownPlayerCollectionJpaRepository extends JpaRepository<UnknownPlayerCollectionEntity, Long> {

    boolean existsByBrawlStarsTag(String brawlStarsTag);

    List<UnknownPlayerCollectionEntity> findAllByStatusInAndDeletedFalseOrderByCreatedAtAsc(
            Collection<UnknownPlayerStatus> statuses, Limit limit);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<UnknownPlayerCollectionEntity> findWithOptimisticLockByBrawlStarsTag(String brawlStarsTag);
}
