package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.imstargg.storage.db.core.player.QBattleCollectionEntity.battleCollectionEntity;

public class BattleItemReader {

    private static final int PAGE_SIZE = 1000;

    private final EntityManagerFactory entityManagerFactory;
    private final OffsetDateTime fromBattleTime;
    private final OffsetDateTime toBattleTime;

    @Nullable
    private BattleCollectionEntity lastEntity;

    public BattleItemReader(
            EntityManagerFactory entityManagerFactory,
            OffsetDateTime fromBattleTime,
            OffsetDateTime toBattleTime,
            @Nullable BattleCollectionEntity lastEntity
    ) {
        this.entityManagerFactory = entityManagerFactory;
        this.fromBattleTime = fromBattleTime;
        this.toBattleTime = toBattleTime;
        this.lastEntity = lastEntity;
    }

    public List<BattleCollectionEntity> read() {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<BattleCollectionEntity> battleEntities = queryFactory
                .selectFrom(battleCollectionEntity)
                .join(battleCollectionEntity.player.player).fetchJoin()
                .where(from(), to())
                .orderBy(battleCollectionEntity.battleTime.asc(), battleCollectionEntity.id.asc())
                .limit(PAGE_SIZE)
                .fetch();
        if (!battleEntities.isEmpty()) {
            lastEntity = battleEntities.getLast();
        }
        return battleEntities;
    }

    private BooleanExpression from() {
        return Optional.ofNullable(lastEntity)
                .map(entity ->
                        battleCollectionEntity.battleTime.eq(entity.getBattleTime())
                                .and(battleCollectionEntity.id.gt(entity.getId()))
                                .or(battleCollectionEntity.battleTime.gt(entity.getBattleTime()))
                ).orElse(battleCollectionEntity.battleTime.goe(fromBattleTime));
    }

    private BooleanExpression to() {
        return battleCollectionEntity.battleTime.lt(toBattleTime);
    }
}
