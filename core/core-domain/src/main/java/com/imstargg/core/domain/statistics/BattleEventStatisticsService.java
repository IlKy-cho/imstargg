package com.imstargg.core.domain.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleEventStatisticsService {

    private final BattleEventStatisticsReader battleEventStatisticsReader;

    public BattleEventStatisticsService(BattleEventStatisticsReader battleEventStatisticsReader) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
    }

    public List<BattleEventBrawlerResultCount> getBattleEventBrawlerResultStatistics(BattleEventBrawlerResultStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlerResultStatistics(param);
    }

    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(BattleEventBrawlersResultStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersResultStatistics(param);
    }

    public List<BattleEventBrawlerRankStatistics> getBattleEventBrawlerRankStatistics(BattleEventBrawlerRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlerRankStatistics(param);
    }

    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(BattleEventBrawlersRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersRankStatistics(param);
    }
}
