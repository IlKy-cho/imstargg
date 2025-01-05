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

    @Cacheable(key = "'battle-event-brawler-result-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTier' + #param.soloRankTier() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public List<BattleEventBrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return battleEventStatisticsRepository.findBrawlerResultStatistics(
                param.eventBrawlStarsId(),
                param.battleDate(),
                param.trophyRange(),
                param.soloRankTier(),
                param.duplicateBrawler()
        );
    }

    @Cacheable(key = "'battle-event-brawlers-result-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTier' + #param.soloRankTier()  + ':brawlersNum' + #param.brawlersNum() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParam param
    ) {
        return battleEventStatisticsRepository.findBrawlersResultStatistics(
                param.eventBrawlStarsId(),
                param.battleDate(),
                param.trophyRange(),
                param.soloRankTier(),
                param.brawlersNum(),
                param.duplicateBrawler()
        );
    }
}
