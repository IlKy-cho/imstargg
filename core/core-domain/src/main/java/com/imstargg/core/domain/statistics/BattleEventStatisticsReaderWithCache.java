package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = CacheNames.STATISTICS)
public class BattleEventStatisticsReaderWithCache {

    private final BattleEventResultStatisticsRepository battleEventResultStatisticsRepository;
    private final BattleEventRankStatisticsRepository battleEventRankStatisticsRepository;

    public BattleEventStatisticsReaderWithCache(
            BattleEventResultStatisticsRepository battleEventResultStatisticsRepository,
            BattleEventRankStatisticsRepository battleEventRankStatisticsRepository
    ) {
        this.battleEventResultStatisticsRepository = battleEventResultStatisticsRepository;
        this.battleEventRankStatisticsRepository = battleEventRankStatisticsRepository;
    }

    @Cacheable(key = "'battle-event-brawler-result-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTier' + #param.soloRankTier() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public List<BattleEventBrawlerResultCount> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return battleEventResultStatisticsRepository.findBrawlerResultCounts(
                param.eventBrawlStarsId(),
                param.battleDate(),
                param.trophyRange(),
                param.soloRankTierRange(),
                param.duplicateBrawler()
        );
    }

    @Cacheable(key = "'battle-event-brawlers-result-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTier' + #param.soloRankTier()  + ':brawlersNum' + #param.brawlersNum() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParam param
    ) {
        return battleEventResultStatisticsRepository.findBrawlersResultStatistics(
                param.eventBrawlStarsId(),
                param.battleDate(),
                param.trophyRange(),
                param.soloRankTierRange(),
                param.brawlersNum(),
                param.duplicateBrawler()
        );
    }

    @Cacheable(key = "'battle-event-brawler-rank-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange()")
    public List<BattleEventBrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return battleEventRankStatisticsRepository.findBrawlerRankStatistics(param);
    }

    @Cacheable(key = "'battle-event-brawlers-rank-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':brawlersNum' + #param.brawlersNum()")
    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParam param
    ) {
        return battleEventRankStatisticsRepository.findBrawlersRankStatistics(param);
    }
}
