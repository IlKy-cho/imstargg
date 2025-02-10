package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BrawlerReader;
import com.imstargg.core.domain.statistics.BattleEventResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlersResultStatistics;
import com.imstargg.core.enums.TrophyRange;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlerStatisticsService {

    private static final int MINIMUM_BATTLE_COUNT = 10;

    private final BrawlerReader brawlerReader;
    private final BrawlerStatisticsReaderWithCache brawlerStatisticsReader;
    private final BrawlerOwnershipReaderWithCache brawlerOwnershipReader;

    public BrawlerStatisticsService(
            BrawlerReader brawlerReader,
            BrawlerStatisticsReaderWithCache brawlerStatisticsReader,
            BrawlerOwnershipReaderWithCache brawlerOwnershipReader
    ) {
        this.brawlerReader = brawlerReader;
        this.brawlerStatisticsReader = brawlerStatisticsReader;
        this.brawlerOwnershipReader = brawlerOwnershipReader;
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

    public BrawlerItemOwnership getOwnershipRate(BrawlStarsId id, TrophyRange trophyRange) {
        return brawlerOwnershipReader.get(brawlerReader.get(id), trophyRange);
    }
}
