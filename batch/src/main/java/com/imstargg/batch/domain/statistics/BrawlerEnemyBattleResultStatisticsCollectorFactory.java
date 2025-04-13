package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerEnemyBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerEnemyBattleResultStatisticsCollectorFactory.class);

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerEnemyBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerEnemyBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerEnemyBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    @Override
    public StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> create(
            LocalDate battleDate
    ) {
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        log.debug("Total {} StatisticsEntity fetched for date[{}]", statsEntities.size(), battleDate);
        return new BrawlerEnemyBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}