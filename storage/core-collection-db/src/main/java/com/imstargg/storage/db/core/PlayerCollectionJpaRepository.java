package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PlayerCollectionJpaRepository extends JpaRepository<PlayerCollectionEntity, Long> {

    List<PlayerCollectionEntity> findAllByDeletedFalseAndStatusInOrderByUpdateWeight(
            Collection<PlayerStatus> status, Limit limit);
}
