package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BrawlerReader;
import com.imstargg.core.domain.statistics.BattleEventResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.enums.TrophyRange;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlerStatisticsService {

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
        return brawlerStatisticsReader.getBrawlerResultStatistics(params);
    }

    public List<BattleEventResultStatistics> getBrawlerBattleEventResultStatistics(
            BrawlerBattleEventResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerBattleEventResultStatistics(param);
    }

    public List<BrawlerPairResultStatistics> getBrawlerBrawlersResultStatistics(
            BrawlerPairResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerPairResultStatistics(param);
    }

    public List<BrawlerPairResultStatistics> getBrawlerEnemyResultStatistics(
            BrawlerEnemyResultStatisticsParam param
    ) {
        return brawlerStatisticsReader.getBrawlerEnemyResultStatistics(param);
    }

    public BrawlerItemOwnership getOwnershipRate(BrawlStarsId id, TrophyRange trophyRange) {
        return brawlerOwnershipReader.get(brawlerReader.get(id), trophyRange);
    }
}
