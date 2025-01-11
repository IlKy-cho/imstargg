package com.imstargg.core.domain.statistics;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@Async
public class BattleEventStatisticsReaderWithAsync {

    private final BattleEventStatisticsReaderWithCache reader;

    public BattleEventStatisticsReaderWithAsync(
            BattleEventStatisticsReaderWithCache reader
    ) {
        this.reader = reader;
    }

    public Future<BattleEventBrawlerResultCounts> getBattleEventBrawlerResultCounts(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlerResultCounts(param));
    }

    public Future<BattleEventBrawlersResultCounts> getBattleEventBrawlersResultCounts(
            BattleEventBrawlersResultStatisticsParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlersResultCounts(param));
    }

    public Future<BattleEventBrawlerRankCounts> getBattleEventBrawlerRankCounts(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlerRankCounts(param));
    }
}
