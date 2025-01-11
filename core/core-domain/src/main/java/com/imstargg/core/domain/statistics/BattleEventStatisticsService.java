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

    private final BattleEventStatisticsReaderWithCache battleEventStatisticsReader;
    private final BattleEventStatisticsReaderWithAsync battleEventStatisticsReaderWithAsync;

    public BattleEventStatisticsService(
            BattleEventStatisticsReaderWithCache battleEventStatisticsReader,
            BattleEventStatisticsReaderWithAsync battleEventStatisticsReaderWithAsync
    ) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
        this.battleEventStatisticsReaderWithAsync = battleEventStatisticsReaderWithAsync;
    }

    @Cacheable(key = "'battle-event-brawler-result-stats:v1:events:' + #params.eventId().value() + ':date' + #params.battleDate() + ':trophyRange' + #params.trophyRangeRange() + ':soloRankTierRange' + #params.soloRankTierRangeRange() + ':duplicateBrawler' + #params.duplicateBrawler()")
    public List<BattleEventBrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParams params
    ) {
        List<BattleEventBrawlerResultCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReaderWithAsync::getBattleEventBrawlerResultCounts)
                .toList());

        BattleEventBrawlerResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlerResultCounts::merge)
                .orElseGet(BattleEventBrawlerResultCounts::empty);

        return mergedCounts.toStatistics()
                .stream()
                .filter(stats -> stats.totalBattleCount() > MINIMUM_BATTLE_COUNT)
                .toList();
    }

    @Cacheable(key = "'battle-event-brawlers-result-stats:v1:events:' + #params.eventId().value() + ':date' + #params.battleDate() + ':trophyRange' + #params.trophyRangeRange() + ':soloRankTierRange' + #params.soloRankTierRangeRange() + ':brawlersNum' + #params.brawlersNum() + ':duplicateBrawler' + #params.duplicateBrawler()")
    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParams params) {
        List<BattleEventBrawlersResultCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReaderWithAsync::getBattleEventBrawlersResultCounts)
                .toList());

        BattleEventBrawlersResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlersResultCounts::merge)
                .orElseGet(BattleEventBrawlersResultCounts::empty);

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

    public List<BattleEventBrawlerRankStatistics> getBattleEventBrawlerRankStatistics(BattleEventBrawlerRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlerRankStatistics(param);
    }

    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(BattleEventBrawlersRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersRankStatistics(param);
    }
}
