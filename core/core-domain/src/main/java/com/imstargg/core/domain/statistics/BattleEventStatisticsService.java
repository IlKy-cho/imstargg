package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.utils.FutureUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = CacheNames.STATISTICS)
public class BattleEventStatisticsService {

    private static final int MINIMUM_BATTLE_COUNT = 10;

    private final BattleEventStatisticsReaderWithAsync battleEventStatisticsReader;

    public BattleEventStatisticsService(
            BattleEventStatisticsReaderWithAsync battleEventStatisticsReader
    ) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
    }

    @Cacheable(key = "'battle-event-brawler-result-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange() + ':soloRankTierRange' + #params.soloRankTierRangeRange() + ':duplicateBrawler' + #params.duplicateBrawler()")
    public List<BrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParams params
    ) {
        List<BrawlerResultCounts> countsList = FutureUtils.get(params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlerResultCounts)
                .toList());

        BrawlerResultCounts mergedCounts = countsList.stream()
                .reduce(BrawlerResultCounts::merge)
                .orElseGet(BrawlerResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawlers-result-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange() + ':soloRankTierRange' + #params.soloRankTierRangeRange() + ':brawlersNum' + #params.brawlersNum() + ':duplicateBrawler' + #params.duplicateBrawler()")
    public List<BrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParams params) {
        List<BrawlersResultCounts> countsList = FutureUtils.get(params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlersResultCounts)
                .toList());

        BrawlersResultCounts mergedCounts = countsList.stream()
                .reduce(BrawlersResultCounts::merge)
                .orElseGet(BrawlersResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawler-rank-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange()")
    public List<BrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParams params
    ) {
        List<BrawlerRankCounts> countsList = FutureUtils.get(params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlerRankCounts)
                .toList());

        BrawlerRankCounts mergedCounts = countsList.stream()
                .reduce(BrawlerRankCounts::merge)
                .orElseGet(BrawlerRankCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawlers-rank-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange() + ':brawlersNum' + #params.brawlersNum()")
    public List<BrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParams params) {
        List<BrawlersRankCounts> countsList = FutureUtils.get(params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlersRankCounts)
                .toList());

        BrawlersRankCounts mergedCounts = countsList.stream()
                .reduce(BrawlersRankCounts::merge)
                .orElseGet(BrawlersRankCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

}
