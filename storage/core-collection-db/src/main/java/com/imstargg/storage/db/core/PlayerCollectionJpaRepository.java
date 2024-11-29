package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;

public interface PlayerCollectionJpaRepository extends JpaRepository<PlayerCollectionEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    List<PlayerCollectionEntity> findAllWithOptimisticLockByDeletedFalseAndStatusInOrderByUpdateWeight(
            Collection<PlayerStatus> statuses, Limit limit);
}
