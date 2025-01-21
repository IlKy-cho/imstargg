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

    public Future<BrawlerResultCounts> getBattleEventBrawlerResultCounts(
            BattleEventBrawlerResultCountParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlerResultCounts(param));
    }

    public Future<BrawlersResultCounts> getBattleEventBrawlersResultCounts(
            BattleEventBrawlersResultCountParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlersResultCounts(param));
    }

    public Future<BrawlerRankCounts> getBattleEventBrawlerRankCounts(
            BattleEventBrawlerRankCountParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlerRankCounts(param));
    }

    public Future<BrawlersRankCounts> getBattleEventBrawlersRankCounts(
            BattleEventBrawlersRankCountParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlersRankCounts(param));
    }
}
