package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerBattleResultStatisticsCollectorFactory {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    public BrawlerBattleResultStatisticsCollector create(
            LocalDate battleDate
    ) {
        List<BrawlerBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        return new BrawlerBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}