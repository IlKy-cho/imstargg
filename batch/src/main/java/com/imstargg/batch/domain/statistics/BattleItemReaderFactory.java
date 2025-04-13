package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

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

        return new BattleItemReader(entityManagerFactory, fromBattleTime, toBattleTime, checkPoint.getBattleId());
    }
}
