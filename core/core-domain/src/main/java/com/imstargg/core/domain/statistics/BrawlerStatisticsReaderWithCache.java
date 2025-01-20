package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = CacheNames.STATISTICS)
public class BrawlerStatisticsReaderWithCache {

    private final BrawlerResultStatisticsRepository brawlerResultStatisticsRepository;

    public BrawlerStatisticsReaderWithCache(
            BrawlerResultStatisticsRepository brawlerResultStatisticsRepository
    ) {
        this.brawlerResultStatisticsRepository = brawlerResultStatisticsRepository;
    }

    @Cacheable(key = "'brawler-result-counts:v1:date' + #param.date() + ':trophyRange' + #param.trophyRange() + ':soloRankTierRange' + #param.soloRankTierRange()")
    public BrawlerResultCounts getBrawlerResultCounts(BrawlerResultStatisticsParam param) {
        return new BrawlerResultCounts(
                brawlerResultStatisticsRepository.findBrawlerResultCounts(
                        param.date(),
                        param.trophyRange(),
                        param.soloRankTierRange()
                )
        );
    }
}
