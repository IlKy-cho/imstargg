package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerPairBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerPairBattleRankStatisticsCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerPairBattleRankStatisticsCollectorFactory.class);

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerPairBattleRankStatisticsCollectionJpaRepository statsRepository;

    public BrawlerPairBattleRankStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerPairBattleRankStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    @Override
    public StatisticsCollector<BrawlerPairBattleRankStatisticsCollectionEntity> create(
            LocalDate battleDate
    ) {
        List<BrawlerPairBattleRankStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        log.debug("Total {} StatisticsEntity fetched for date[{}]", statsEntities.size(), battleDate);
        return new BrawlerPairBattleRankStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}