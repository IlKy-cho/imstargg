package com.imstargg.storage.db.core;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface BattleCollectionJpaRepository extends JpaRepository<BattleCollectionEntity, Long> {

    @EntityGraph(attributePaths = {"player.player"})
    Slice<BattleCollectionEntity> findSliceWithPlayerByBattleTimeGreaterThanEqualAndBattleTimeLessThan(
            OffsetDateTime fromBattleTime, OffsetDateTime toBattleTime, Pageable pageable);

    @EntityGraph(attributePaths = {"player.player"})
    Slice<BattleCollectionEntity> findSliceWithPlayerByEventBrawlStarsIdAndBattleTimeGreaterThanEqualAndBattleTimeLessThan(
            long eventBrawlStarsId, OffsetDateTime fromBattleTime, OffsetDateTime toBattleTime, Pageable pageable);

    Optional<BattleCollectionEntity> findFirst1ByOrderByIdDesc();
}
