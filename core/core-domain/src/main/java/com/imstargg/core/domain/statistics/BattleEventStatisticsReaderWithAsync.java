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

    public Future<BattleEventBrawlerResultCounts> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return CompletableFuture.completedFuture(reader.getBattleEventBrawlerResultStatistics(param));
    }
}
