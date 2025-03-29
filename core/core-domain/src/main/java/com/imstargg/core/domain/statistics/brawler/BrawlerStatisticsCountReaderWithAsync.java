package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.BattleEventResultCounts;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlerPairResultCounts;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Async
@Component
public class BrawlerStatisticsCountReaderWithAsync {

    private final BrawlerResultStatisticsRepository brawlerResultStatisticsRepository;

    public BrawlerStatisticsCountReaderWithAsync(
            BrawlerResultStatisticsRepository brawlerResultStatisticsRepository
    ) {
        this.brawlerResultStatisticsRepository = brawlerResultStatisticsRepository;
    }

    public Future<BrawlerResultCounts> getBrawlerResultCounts(BrawlerResultCountParam param) {
        return CompletableFuture.completedFuture(
                new BrawlerResultCounts(
                        brawlerResultStatisticsRepository.findBrawlerResultCounts(
                                param.date(),
                                param.trophyRange(),
                                param.soloRankTierRange()
                        )
                )
        );
    }

    public Future<BattleEventResultCounts> getBrawlerBattleEventResultCounts(BrawlerBattleEventResultCountParam param) {
        return CompletableFuture.completedFuture(
                new BattleEventResultCounts(
                        brawlerResultStatisticsRepository.findBrawlerBattleEventResultCounts(
                                param.brawlerId(),
                                param.date(),
                                param.trophyRange(),
                                param.soloRankTierRange()
                        )
                )
        );
    }

    public Future<BrawlerPairResultCounts> getBrawlerBrawlersResultCounts(BrawlerBrawlersResultCountParam param) {
        return CompletableFuture.completedFuture(
                new BrawlerPairResultCounts(
                        brawlerResultStatisticsRepository.findBrawlerBrawlersResultCounts(
                                param.brawlerId(),
                                param.date(),
                                param.trophyRange(),
                                param.soloRankTierRange(),
                                param.brawlersNum()
                        )
                )
        );
    }

    public Future<BrawlerEnemyResultCounts> getBrawlerEnemyResultCounts(BrawlerEnemyResultCountParam param) {
        return CompletableFuture.completedFuture(
                new BrawlerEnemyResultCounts(
                        brawlerResultStatisticsRepository.findBrawlerEnemyResultCounts(
                                param.brawlerId(),
                                param.date(),
                                param.trophyRange(),
                                param.soloRankTierRange()
                        )
                )
        );
    }
}
