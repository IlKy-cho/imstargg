package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerPairBattleResultStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerPairBattleResultStatisticsCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerPairBattleResultStatisticsCollectorFactory.class);

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerPairBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerPairBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerPairBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    @Override
    public StatisticsCollector<BrawlerPairBattleResultStatisticsCollectionEntity> create(
            LocalDate battleDate
    ) {
        List<BrawlerPairBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        log.debug("Total {} StatisticsEntity fetched for date[{}]", statsEntities.size(), battleDate);
        return new BrawlerPairBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}