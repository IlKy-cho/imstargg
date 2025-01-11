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

    @Cacheable(key = "'battle-event-brawler-result-counts:v1:events:' + #param.eventId().value() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTierRange' + #param.soloRankTierRange() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public BattleEventBrawlerResultCounts getBattleEventBrawlerResultCounts(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return new BattleEventBrawlerResultCounts(
                battleEventResultStatisticsRepository.findBrawlerResultCounts(
                        param.eventId(),
                        param.battleDate(),
                        param.trophyRange(),
                        param.soloRankTierRange(),
                        param.duplicateBrawler()
                )
        );
    }

    @Cacheable(key = "'battle-event-brawlers-result-counts:v1:events:' + #param.eventId().value() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':soloRankTierRange' + #param.soloRankTierRange()  + ':brawlersNum' + #param.brawlersNum() + ':duplicateBrawler' + #param.duplicateBrawler()")
    public BattleEventBrawlersResultCounts getBattleEventBrawlersResultCounts(
            BattleEventBrawlersResultStatisticsParam param
    ) {
        return new BattleEventBrawlersResultCounts(
                battleEventResultStatisticsRepository.findBrawlersResultCounts(
                        param.eventId(),
                        param.battleDate(),
                        param.trophyRange(),
                        param.soloRankTierRange(),
                        param.brawlersNum(),
                        param.duplicateBrawler()
                )
        );
    }

    @Cacheable(key = "'battle-event-brawler-rank-counts:v1:events:' + #param.eventId().value() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange()")
    public BattleEventBrawlerRankCounts getBattleEventBrawlerRankCounts(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return new BattleEventBrawlerRankCounts(
                battleEventRankStatisticsRepository.findBrawlerRankCounts(
                        param.eventId(),
                        param.battleDate(),
                        param.trophyRange()
                )
        );
    }

    @Cacheable(key = "'battle-event-brawlers-rank-stats:v1:events:' + #param.eventBrawlStarsId() + ':date' + #param.battleDate() + ':trophyRange' + #param.trophyRange() + ':brawlersNum' + #param.brawlersNum()")
    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParam param
    ) {
        return battleEventRankStatisticsRepository.findBrawlersRankStatistics(param);
    }
}
