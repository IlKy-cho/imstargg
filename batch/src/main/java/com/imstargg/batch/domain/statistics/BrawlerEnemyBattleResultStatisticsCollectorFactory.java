package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BrawlerEnemyBattleResultStatisticsCollectorFactory {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerEnemyBattleResultStatisticsCollectionJpaRepository statsRepository;

    public BrawlerEnemyBattleResultStatisticsCollectorFactory(
            BattleStatisticsCollectionValidator validator,
            BrawlerEnemyBattleResultStatisticsCollectionJpaRepository statsRepository
    ) {
        this.validator = validator;
        this.statsRepository = statsRepository;
    }

    public BrawlerEnemyBattleResultStatisticsCollector create(
            LocalDate battleDate
    ) {
        List<BrawlerEnemyBattleResultStatisticsCollectionEntity> statsEntities = statsRepository.findAllByBattleDate(battleDate);
        return new BrawlerEnemyBattleResultStatisticsCollector(
                validator,
                battleDate,
                statsEntities
        );
    }
}