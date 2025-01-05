package com.imstargg.core.domain.statistics;

import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleEventRankStatisticsRepository {

    private static final int PAGE_SIZE = 1000;

    private final BrawlerBattleRankStatisticsJpaRepository brawlerBattleRankStatisticsJpaRepository;
    private final BrawlersBattleRankStatisticsJpaRepository brawlersBattleRankStatisticsJpaRepository;

    public BattleEventRankStatisticsRepository(
            BrawlerBattleRankStatisticsJpaRepository brawlerBattleRankStatisticsJpaRepository,
            BrawlersBattleRankStatisticsJpaRepository brawlersBattleRankStatisticsJpaRepository
    ) {
        this.brawlerBattleRankStatisticsJpaRepository = brawlerBattleRankStatisticsJpaRepository;
        this.brawlersBattleRankStatisticsJpaRepository = brawlersBattleRankStatisticsJpaRepository;
    }

    public List<BattleEventBrawlerRankStatistics> findBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return brawlerBattleRankStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRange(
                        param.eventBrawlStarsId(), param.battleDate(), param.trophyRange()
                ).stream()
                .map(statsEntity -> new BattleEventBrawlerRankStatistics(
                        statsEntity.getBrawlerBrawlStarsId(),
                        statsEntity.getRankToCounts()
                )).toList();
    }
}
