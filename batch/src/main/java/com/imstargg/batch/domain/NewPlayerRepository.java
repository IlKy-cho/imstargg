package com.imstargg.batch.domain;

import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Limit;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewPlayerRepository {

    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public NewPlayerRepository(UnknownPlayerCollectionJpaRepository unknownPlayerRepository) {
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class)
    public List<UnknownPlayerCollectionEntity> find(int size) {
        List<UnknownPlayerCollectionEntity> players = unknownPlayerRepository.findAllWithOptimisticLockByStatusInAndDeletedFalseOrderByCreatedAtAsc(
                Limit.of(size), List.of(UnknownPlayerStatus.ADMIN_NEW, UnknownPlayerStatus.UPDATE_NEW)
        );
        players.forEach(player -> player.setStatus(UnknownPlayerStatus.UPDATING));
        return players;
    }
}
