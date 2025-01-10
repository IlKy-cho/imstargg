package com.imstargg.core.domain.statistics;

import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@Service
public class BattleEventStatisticsService {

    private final BattleEventStatisticsReaderWithCache battleEventStatisticsReader;
    private final BattleEventStatisticsReaderWithAsync battleEventStatisticsReaderWithAsync;

    public BattleEventStatisticsService(
            BattleEventStatisticsReaderWithCache battleEventStatisticsReader,
            BattleEventStatisticsReaderWithAsync battleEventStatisticsReaderWithAsync
    ) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
        this.battleEventStatisticsReaderWithAsync = battleEventStatisticsReaderWithAsync;
    }

    public List<BattleEventBrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParams params
    ) {
        List<BattleEventBrawlerResultCounts> countsList = getFutureResults(() -> params.toParamList().stream()
                .map(battleEventStatisticsReaderWithAsync::getBattleEventBrawlerResultStatistics)
                .toList());

        BattleEventBrawlerResultCounts mergedCounts = countsList.stream()
                .reduce(BattleEventBrawlerResultCounts::merge)
                .orElseGet(BattleEventBrawlerResultCounts::empty);

        return mergedCounts.toStatistics();

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

    public List<BattleEventBrawlersResultStatistics> getBattleEventBrawlersResultStatistics(BattleEventBrawlersResultStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersResultStatistics(param);
    }

    public List<BattleEventBrawlerRankStatistics> getBattleEventBrawlerRankStatistics(BattleEventBrawlerRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlerRankStatistics(param);
    }

    public List<BattleEventBrawlersRankStatistics> getBattleEventBrawlersRankStatistics(BattleEventBrawlersRankStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventBrawlersRankStatistics(param);
    }
}
