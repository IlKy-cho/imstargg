package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.utils.FutureUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = CacheNames.STATISTICS)
public class BrawlerStatisticsService {

    private static final int MINIMUM_BATTLE_COUNT = 10;

    private final BrawlerStatisticsReaderWithAsync brawlerStatisticsReader;

    public BrawlerStatisticsService(BrawlerStatisticsReaderWithAsync brawlerStatisticsReader) {
        this.brawlerStatisticsReader = brawlerStatisticsReader;
    }

    @Cacheable(key = "'brawler-result-stats:v1:date' + #params.date() + ':trophyRange' + #params.trophyRange() + ':soloRankTierRange' + #params.soloRankTierRange()")
    public List<BrawlerResultStatistics> getBrawlerResultStatistics(
            BrawlerResultStatisticsParam params
    ) {
        List<BrawlerResultCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                .map(brawlerStatisticsReader::getBrawlerResultCounts)
                .toList());

        BrawlerResultCounts mergedCounts = countsList.stream()
                .reduce(BrawlerResultCounts::merge)
                .orElseGet(BrawlerResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'brawler-battle-event-result-stats:v1:date' + #params.date() + ':trophyRange' + #params.trophyRange() + ':soloRankTierRange' + #params.soloRankTierRange()")
    public List<BattleEventResultStatistics> getBrawlerBattleEventResultStatistics(
            BrawlerBattleEventResultStatisticsParam params
    ) {
        List<BattleEventResultCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                .map(brawlerStatisticsReader::getBrawlerBattleEventResultCounts)
                .toList());

        BattleEventResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventResultCounts::merge)
                .orElseGet(BattleEventResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'brawler-brawlers-result-stats:v1:date' + #params.date() + ':brawlerId' + #params.brawlerId() + ':trophyRange' + #params.trophyRange() + ':soloRankTierRange' + #params.soloRankTierRange()")
    public List<BrawlersResultStatistics> getBrawlerBrawlersResultStatistics(
            BrawlerBrawlersResultStatisticsParam params
    ) {
        List<BrawlersResultCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                .map(brawlerStatisticsReader::getBrawlerBrawlersResultCounts)
                .toList());

        BrawlersResultCounts mergedCounts = countsList.stream()
                .reduce(BrawlersResultCounts::merge)
                .orElseGet(BrawlersResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }
}
