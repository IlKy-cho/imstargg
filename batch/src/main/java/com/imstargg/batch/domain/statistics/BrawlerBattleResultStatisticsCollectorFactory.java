package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleResultStatisticsCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerBattleResultStatisticsCollectorFactory.class);

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    @Override
    public StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> create(
            LocalDate battleDate
    ) {
        List<BrawlerBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        log.debug("Total {} StatisticsEntity fetched for date[{}]", statsEntities.size(), battleDate);
        return new BrawlerBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}