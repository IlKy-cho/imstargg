package com.imstargg.batch.domain;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

public class BattleReader {

    private final Clock clock;

    private final EntityManagerFactory entityManagerFactory;

    @Nullable
    private BattleCollectionEntity lastBattle;

    public BattleReader(Clock clock, EntityManagerFactory entityManagerFactory) {
        this.clock = clock;
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<BattleCollectionEntity> read(
            LocalDate date, int size) {
        JPAQueryFactory queryFactory = JPAQueryFactoryUtils.getQueryFactory(entityManagerFactory);
        List<BattleCollectionEntity> result = queryFactory
                .selectFrom(battleCollectionEntity)
                .join(battleCollectionEntity.player.player).fetchJoin()
                .where(
                        battleCollectionEntity.battleTime.goe(date.atStartOfDay().atZone(clock.getZone()).toOffsetDateTime()),
                        battleCollectionEntity.battleTime.lt(date.plusDays(1).atStartOfDay().atZone(clock.getZone()).toOffsetDateTime()),
                        lastBattleCondition()
                )
                .orderBy(battleCollectionEntity.battleTime.desc(), battleCollectionEntity.id.asc())
                .limit(size)
                .fetch();
        if (!result.isEmpty()) {
            lastBattle = result.getLast();
        }
        return result;
    }

    @Nullable
    private BooleanExpression lastBattleCondition() {
        if (lastBattle == null) {
            return null;
        }

        return battleCollectionEntity.battleTime.eq(lastBattle.getBattleTime()).and(
                        battleCollectionEntity.id.gt(lastBattle.getId()))
                .or(battleCollectionEntity.battleTime.lt(lastBattle.getBattleTime()));
    }
}
