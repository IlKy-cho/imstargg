package com.imstargg.storage.db.core;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PlayerCollectionJpaRepository extends JpaRepository<PlayerCollectionEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @EntityGraph(attributePaths = {"brawlers"})
    Optional<PlayerCollectionEntity> findVersionedWithBrawlersByBrawlStarsTag(String brawlStarsTag);

    Optional<PlayerCollectionEntity> findFirst1ByOrderByIdDesc();
}
