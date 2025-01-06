package com.imstargg.storage.db.core;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.imstargg.storage.db.core.QBattleEntity.battleEntity;

class BattleJpaRepositoryCustomImpl implements BattleJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> findAllDistinctEventBrawlStarsIdsByGreaterThanEqualBattleTime(@Nullable LocalDateTime battleTime) {
        return queryFactory
                .select(
                        battleEntity.event.brawlStarsId,
                        battleEntity.battleTime.max()
                )
                .from(battleEntity)
                .groupBy(battleEntity.event.brawlStarsId)
                .having(battleTime == null ? null : battleEntity.battleTime.max().goe(battleTime))
                .fetch()
                .stream()
                .filter(tuple -> {
                    Long eventBrawlStarsId = tuple.get(battleEntity.event.brawlStarsId);
                    return eventBrawlStarsId != null && eventBrawlStarsId > 0;
                })
                .map(tuple -> tuple.get(battleEntity.event.brawlStarsId))
                .toList()
                ;
    }

    @Override
    public Optional<BattleEntity> findLatestBattle(long eventBrawlStarsId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(battleEntity)
                        .where(battleEntity.event.brawlStarsId.eq(eventBrawlStarsId))
                        .orderBy(battleEntity.battleTime.desc())
                        .fetchFirst()
        );
    }
}
