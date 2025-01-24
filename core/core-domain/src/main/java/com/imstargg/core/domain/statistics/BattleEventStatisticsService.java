package com.imstargg.core.domain.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleEventStatisticsService {

    private static final int MINIMUM_BATTLE_COUNT = 10;

    private final BattleEventStatisticsReaderWithCache battleEventStatisticsReader;

    public BattleEventStatisticsService(
            BattleEventStatisticsReaderWithCache battleEventStatisticsReader
    ) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
    }

    public List<BrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerResultStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    public List<BrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlersResultStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    public List<BrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerRankStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }


    public List<BrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersRankStatistics(param)
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

}
