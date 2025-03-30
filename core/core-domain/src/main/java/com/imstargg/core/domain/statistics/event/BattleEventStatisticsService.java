package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.statistics.BrawlerRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleEventStatisticsService {

    private final BattleEventStatisticsReaderWithCache battleEventStatisticsReader;

    public BattleEventStatisticsService(
            BattleEventStatisticsReaderWithCache battleEventStatisticsReader
    ) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
    }

    public List<BrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerResultStatistics(param);
    }

    public List<BrawlerPairResultStatistics> getBattleEventBrawlerPairResultStatistics(
            BattleEventBrawlerPairResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerPairResultStatistics(param);
    }

    public List<BrawlerPairResultStatistics> getBattleEventBrawlerEnemyResultStatistics(
            BattleEventBrawlerEnemyResultStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerEnemyResultStatistics(param);
    }

    public List<BrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlerRankStatistics(param);
    }


    public List<BrawlerPairRankStatistics> getBattleEventBrawlerPairRankStatistics(
            BattleEventBrawlerPairRankStatisticsParam param
    ) {
        return battleEventStatisticsReader.getBattleEventBrawlersRankStatistics(param);
    }

}
