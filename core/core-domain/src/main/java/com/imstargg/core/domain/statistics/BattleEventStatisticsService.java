package com.imstargg.core.domain.statistics;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.error.CoreException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

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
    public List<BattleEventBrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParams params
    ) {
        List<BattleEventBrawlerResultCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlerResultCounts)
                .toList());

        BattleEventBrawlerResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlerResultCounts::merge)
                .orElseGet(BattleEventBrawlerResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawlers-result-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange() + ':soloRankTierRange' + #params.soloRankTierRangeRange() + ':brawlersNum' + #params.brawlersNum() + ':duplicateBrawler' + #params.duplicateBrawler()")
    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParams params) {
        List<BattleEventBrawlersResultCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlersResultCounts)
                .toList());

        BattleEventBrawlersResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlersResultCounts::merge)
                .orElseGet(BattleEventBrawlersResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawler-rank-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange()")
    public List<BattleEventBrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParams params
    ) {
        List<BattleEventBrawlerRankCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlerRankCounts)
                .toList());

        BattleEventBrawlerRankCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlerRankCounts::merge)
                .orElseGet(BattleEventBrawlerRankCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawlers-rank-stats:v1:events:' + #params.eventId().value() + ':date' + #params.date() + ':trophyRange' + #params.trophyRangeRange() + ':brawlersNum' + #params.brawlersNum()")
    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParams params) {
        List<BattleEventBrawlersRankCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReader::getBattleEventBrawlersRankCounts)
                .toList());

        BattleEventBrawlersRankCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlersRankCounts::merge)
                .orElseGet(BattleEventBrawlersRankCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    private <T> List<T> getFutureResults(Supplier<List<Future<T>>> supplier) {
        List<Future<T>> futures = supplier.get();
        List<T> results = new ArrayList<>();
        try {
            for (Future<T> future : futures) {
                results.add(future.get());
            }
        } catch (ExecutionException e) {
            throw new CoreException("Failed to get future results", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CoreException("Failed to get future results", e);
        }
        return results;
    }
}
