package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static com.imstargg.storage.db.core.player.QBattleCollectionEntity.battleCollectionEntity;

public class BattleItemReader {

    private final EntityManagerFactory entityManagerFactory;
    private final OffsetDateTime fromBattleTime;
    private final OffsetDateTime toBattleTime;
    private final long offsetBattleId;

    public BattleItemReader(
            EntityManagerFactory entityManagerFactory,
            OffsetDateTime fromBattleTime,
            OffsetDateTime toBattleTime,
            long offsetBattleId
    ) {
        this.entityManagerFactory = entityManagerFactory;
        this.fromBattleTime = fromBattleTime;
        this.toBattleTime = toBattleTime;
        this.offsetBattleId = offsetBattleId;
    }

    public List<BattleCollectionEntity> read(int page, int size) {
        EntityManager entityManager = Objects.requireNonNull(EntityManagerFactoryUtils.getTransactionalEntityManager(
                entityManagerFactory));
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<BattleCollectionEntity> battleEntities = queryFactory
                .selectFrom(battleCollectionEntity)
                .join(battleCollectionEntity.player.player).fetchJoin()
                .where(
                        battleCollectionEntity.id.gt(offsetBattleId),
                        battleCollectionEntity.battleTime.goe(fromBattleTime),
                        battleCollectionEntity.battleTime.lt(toBattleTime)
                )
                .orderBy(battleCollectionEntity.battleTime.desc())
                .limit(size)
                .offset((long) page * size)
                .fetch();
        battleEntities.forEach(entityManager::detach);
        return battleEntities;
    }

}
