package com.imstargg.storage.db.core;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.imstargg.storage.db.core.QBattleEntity.battleEntity;

class BattleJpaRepositoryCustomImpl implements BattleJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> findAllDistinctEventBrawlStarsIds(@Nullable LocalDate fromDate) {
        return queryFactory.select(battleEntity.event.brawlStarsId)
                .distinct()
                .from(battleEntity)
                .where(
                        battleEntity.event.brawlStarsId.isNotNull(),
                        battleEntity.event.brawlStarsId.gt(0),
                        fromDate != null ? battleEntity.battleTime.goe(fromDate.atStartOfDay()) : null
                )
                .fetch();
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
