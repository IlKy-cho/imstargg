package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = CacheNames.STATISTICS)
public class BattleEventStatisticsReader {

    private final BattleEventStatisticsRepository battleEventStatisticsRepository;

    public BattleEventStatisticsReader(BattleEventStatisticsRepository battleEventStatisticsRepository) {
        this.battleEventStatisticsRepository = battleEventStatisticsRepository;
    }

    @Cacheable(key = "'battle-event-result-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTier' + #param.soloRankTier() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public List<BattleEventBrawlerResultStatistics> getBattleEventResultStatistics(
            BattleEventResultStatisticsParam param
    ) {
        return battleEventStatisticsRepository.findBrawlerResultStatistics(
                param.eventBrawlStarsId(),
                param.battleDate(),
                param.trophyRange(),
                param.soloRankTier(),
                param.duplicateBrawler()
        );
    }
}
