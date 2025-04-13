package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerBattleRankStatisticsCollectorFactory
        implements StatisticsCollectorFactory<BrawlerBattleRankStatisticsCollectionEntity> {

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
        return new BrawlerBattleRankStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}