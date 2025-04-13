package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerPairBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerPairBattleRankStatisticsCollectionEntity> {

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
        return new BrawlerPairBattleRankStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}