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
            BrawlerResultStatisticsParams params
    ) {
        List<BrawlerResultCounts> countsList = FutureUtils.get(params.toParamList().stream()
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
}
