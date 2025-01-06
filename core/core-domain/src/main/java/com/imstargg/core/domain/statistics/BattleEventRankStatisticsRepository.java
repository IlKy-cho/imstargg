package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId()),
                        statsEntity.getRankToCounts()
                )).toList();
    }

    public List<BattleEventBrawlersRankStatistics> findBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParam param
    ) {
        Map<BrawlerIdHash, BattleEventBrawlersCounter> brawlersCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {

            Slice<BrawlersBattleRankStatisticsEntity> brawlersBattleRankStatsSlice = brawlersBattleRankStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndBrawlersNum(
                            param.eventBrawlStarsId(), param.battleDate(), param.trophyRange(), param.brawlersNum(),
                            pageRequest
                    );

            brawlersBattleRankStatsSlice.forEach(stats -> {
                BattleEventBrawlersCounter brawlersCounter = brawlersCounters.computeIfAbsent(
                        new BrawlerIdHash(stats.getBrawlers().getIdHash()),
                        k -> new BattleEventBrawlersCounter()
                );
                brawlersCounter.addRankToCounts(stats.getRankToCounts());
            });

            hasNext = brawlersBattleRankStatsSlice.hasNext();
            pageRequest = pageRequest.next();
        }

        return brawlersCounters.entrySet().stream()
                .map(entry -> {
                    Map<Integer, Long> rankToCounts = new HashMap<>();
                    entry.getValue().getRankToCounts().forEach((rank, count) -> {
                        rankToCounts.put(rank, count / entry.getKey().num());
                    });
                    return new BattleEventBrawlersRankStatistics(
                            entry.getKey().ids()
                                    .stream()
                                    .map(BrawlStarsId::new)
                                    .toList(),
                            rankToCounts
                    );
                }).toList();
    }

    private static class BattleEventBrawlersCounter {
        private final Map<Integer, Long> rankToCounts = new HashMap<>();

        public void addRankToCounts(Map<Integer, Long> rankToCounts) {
            rankToCounts.forEach((rank, count) ->
                    this.rankToCounts.merge(rank, count, Long::sum)
            );
        }

        public Map<Integer, Long> getRankToCounts() {
            return rankToCounts;
        }
    }
}
