package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerPairBattleResultStatisticsCollectorFactory {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerPairBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerPairBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerPairBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    public BrawlerPairBattleResultStatisticsCollector create(
            LocalDate battleDate
    ) {
        List<BrawlerPairBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        return new BrawlerPairBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}