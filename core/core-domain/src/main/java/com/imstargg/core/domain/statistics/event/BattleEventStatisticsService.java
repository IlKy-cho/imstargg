package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.statistics.BrawlerEnemyResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlersRankStatistics;
import com.imstargg.core.domain.statistics.BrawlersResultStatistics;
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

    // 브롤러 & 상대 브롤러 조합으로 나와서
    public List<BrawlerEnemyResultStatistics> getBattleEventBrawlerEnemyResultStatistics(
            BattleEventBrawlerEnemyResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerEnemyResultStatistics(param)
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
