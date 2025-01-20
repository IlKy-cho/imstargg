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

    public Future<BrawlerResultCounts> getBrawlerResultCounts(BrawlerResultStatisticsParam param) {
        return CompletableFuture.completedFuture(reader.getBrawlerResultCounts(param));
    }

    public Future<BattleEventResultCounts> getBrawlerBattleEventResultCounts(BrawlerBattleEventResultStatisticsParam param) {
        return CompletableFuture.completedFuture(reader.getBrawlerBattleEventResultCounts(param));
    }
}
