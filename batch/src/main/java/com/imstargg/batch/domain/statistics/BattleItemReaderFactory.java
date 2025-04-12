package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

import static com.imstargg.storage.db.core.player.QBattleCollectionEntity.battleCollectionEntity;

@Component
public class BattleItemReaderFactory {

    private final Clock clock;
    private final EntityManagerFactory entityManagerFactory;

    public BattleItemReaderFactory(
            Clock clock,
            EntityManagerFactory entityManagerFactory
    ) {
        this.clock = clock;
        this.entityManagerFactory = entityManagerFactory;
    }

    public BattleItemReader create(
            StatisticsCheckPointCollectionEntity checkPoint
    ) {
        OffsetDateTime fromBattleTime = checkPoint.getBattleDate().atStartOfDay(clock.getZone()).toOffsetDateTime();
        OffsetDateTime toBattleTime = fromBattleTime.plusDays(1);

        return new BattleItemReader(entityManagerFactory, fromBattleTime, toBattleTime, fetchLastBattle(checkPoint));
    }

    @Nullable
    private BattleCollectionEntity fetchLastBattle(StatisticsCheckPointCollectionEntity checkPoint) {
        if (checkPoint.getBattleId() <= 0) {
            return null;
        }

        return Objects.requireNonNull(
                new JPAQueryFactory(
                        EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)
                )
                        .selectFrom(battleCollectionEntity)
                        .where(battleCollectionEntity.id.eq(checkPoint.getBattleId()))
                        .fetchOne()
        );
    }
}
