package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleRankStatisticsCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerBattleRankStatisticsCollectorFactory.class);

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerBattleRankStatisticsCollectionJpaRepository statsRepository;

    public BrawlerBattleRankStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerBattleRankStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    @Override
    public StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> create(
            LocalDate battleDate
    ) {
        List<BrawlerBattleRankStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        log.debug("Total {} StatisticsEntity fetched for date[{}]", statsEntities.size(), battleDate);
        return new BrawlerBattleRankStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}