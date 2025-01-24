package com.imstargg.core.domain.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlerStatisticsService {

    private static final int MINIMUM_BATTLE_COUNT = 10;

    private final BrawlerStatisticsReaderWithCache brawlerStatisticsReader;

    public BrawlerStatisticsService(BrawlerStatisticsReaderWithCache brawlerStatisticsReader) {
        this.brawlerStatisticsReader = brawlerStatisticsReader;
    }

    public List<BrawlerResultStatistics> getBrawlerResultStatistics(
            BrawlerResultStatisticsParam params
    ) {
        return brawlerStatisticsReader.getBrawlerResultStatistics(params)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    public List<BattleEventResultStatistics> getBrawlerBattleEventResultStatistics(
            BrawlerBattleEventResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerBattleEventResultStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    public List<BrawlersResultStatistics> getBrawlerBrawlersResultStatistics(
            BrawlerBrawlersResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerBrawlersResultStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    public List<BrawlerEnemyResultStatistics> getBrawlerEnemyResultStatistics(
            BrawlerEnemyResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerEnemyResultStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }
}
