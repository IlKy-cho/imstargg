package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.statistics.BrawlerEnemyResultCounts;
import com.imstargg.core.domain.statistics.BrawlerRankCounts;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlersRankCounts;
import com.imstargg.core.domain.statistics.BrawlersResultCounts;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@Async
public class BattleEventStatisticsCountReaderWithAsync {

    private final BattleEventResultStatisticsRepository battleEventResultStatisticsRepository;
    private final BattleEventRankStatisticsRepository battleEventRankStatisticsRepository;

    public BattleEventStatisticsCountReaderWithAsync(
            BattleEventResultStatisticsRepository battleEventResultStatisticsRepository,
            BattleEventRankStatisticsRepository battleEventRankStatisticsRepository
    ) {
        this.battleEventResultStatisticsRepository = battleEventResultStatisticsRepository;
        this.battleEventRankStatisticsRepository = battleEventRankStatisticsRepository;
    }

    public Future<BrawlerResultCounts> getBattleEventBrawlerResultCounts(
            BattleEventBrawlerResultCountParam param
    ) {
        return CompletableFuture.completedFuture(
                new BrawlerResultCounts(
                        battleEventResultStatisticsRepository.findBrawlerResultCounts(
                                param.eventId(),
                                param.battleDate(),
                                param.trophyRange(),
                                param.soloRankTierRange()
                        )
                )
        );
    }

    public Future<BrawlersResultCounts> getBattleEventBrawlersResultCounts(
            BattleEventBrawlersResultCountParam param
    ) {
        return CompletableFuture.completedFuture(
                new BrawlersResultCounts(
                        battleEventResultStatisticsRepository.findBrawlersResultCounts(
                                param.eventId(),
                                param.battleDate(),
                                param.trophyRange(),
                                param.soloRankTierRange(),
                                param.brawlersNum()
                        )
                )
        );
    }

    public Future<BrawlerRankCounts> getBattleEventBrawlerRankCounts(
            BattleEventBrawlerRankCountParam param
    ) {
        return CompletableFuture.completedFuture(
                new BrawlerRankCounts(
                        battleEventRankStatisticsRepository.findBrawlerRankCounts(
                                param.eventId(),
                                param.battleDate(),
                                param.trophyRange()
                        )
                )
        );
    }

    public Future<BrawlersRankCounts> getBattleEventBrawlersRankCounts(
            BattleEventBrawlersRankCountParam param
    ) {
        return CompletableFuture.completedFuture(
                new BrawlersRankCounts(
                        battleEventRankStatisticsRepository.findBrawlersRankCounts(
                                param.eventId(),
                                param.battleDate(),
                                param.trophyRange(),
                                param.brawlersNum()
                        )
                )
        );
    }

    public Future<BrawlerEnemyResultCounts> getBattleEventBrawlerEnemyResultCounts(
            BattleEventBrawlerEnemyResultCountParam param) {
        return CompletableFuture.completedFuture(
                new BrawlerEnemyResultCounts(
                        battleEventResultStatisticsRepository.findBrawlerEnemyResultCounts(
                                param.eventId(),
                                param.battleDate(),
                                param.trophyRange(),
                                param.soloRankTierRange()
                        )
                )
        );
    }
}
