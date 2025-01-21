package com.imstargg.core.domain.statistics;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@Async
public class BrawlerStatisticsReaderWithAsync {

    private final BrawlerStatisticsReaderWithCache reader;

    public BrawlerStatisticsReaderWithAsync(
            BrawlerStatisticsReaderWithCache reader
    ) {
        this.reader = reader;
    }

    public Future<BrawlerResultCounts> getBrawlerResultCounts(BrawlerResultCountParam param) {
        return CompletableFuture.completedFuture(reader.getBrawlerResultCounts(param));
    }

    public Future<BattleEventResultCounts> getBrawlerBattleEventResultCounts(BrawlerBattleEventResultCountParam param) {
        return CompletableFuture.completedFuture(reader.getBrawlerBattleEventResultCounts(param));
    }

    public Future<BrawlersResultCounts> getBrawlerBrawlersResultCounts(BrawlerBrawlersResultCountParam param) {
        return CompletableFuture.completedFuture(reader.getBrawlerBrawlersResultCounts(param));
    }
}
