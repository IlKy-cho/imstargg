package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerRankCount;
import com.imstargg.core.domain.statistics.BrawlersRankCount;
import com.imstargg.core.domain.statistics.RankCount;
import com.imstargg.core.domain.statistics.RankCounter;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.IdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public List<BrawlerRankCount> findBrawlerRankCounts(
            BrawlStarsId eventId,
            LocalDate battleDate,
            TrophyRange trophyRange
    ) {
        return brawlerBattleRankStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRange(
                        eventId.value(), battleDate, trophyRange
                ).stream()
                .map(statsEntity -> new BrawlerRankCount(
                        new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId()),
                        new RankCount(statsEntity.getRankToCounts())
                )).toList();
    }

    public List<BrawlersRankCount> findBrawlersRankCounts(
            BrawlStarsId eventId, LocalDate battleDate, TrophyRange trophyRange, int brawlersNum
    ) {
        Map<IdHash, RankCounter> brawlersCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {

            Slice<BrawlersBattleRankStatisticsEntity> brawlersBattleRankStatsSlice = brawlersBattleRankStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndBrawlersNum(
                            eventId.value(), battleDate, trophyRange, brawlersNum,
                            pageRequest
                    );

            brawlersBattleRankStatsSlice.forEach(stats -> {
                RankCounter brawlersCounter = brawlersCounters.computeIfAbsent(
                        new IdHash(stats.getBrawlers().getIdHash()),
                        k -> new RankCounter()
                );
                brawlersCounter.add(stats.getRankToCounts());
            });

            hasNext = brawlersBattleRankStatsSlice.hasNext();
            pageRequest = pageRequest.next();
        }

        return brawlersCounters.entrySet().stream()
                .map(entry -> {
                    Map<Integer, Long> rankToCount = new HashMap<>();
                    entry.getValue().getRankToCount().forEach((rank, count) -> {
                        rankToCount.put(rank, count / entry.getKey().num());
                    });
                    return new BrawlersRankCount(
                            entry.getKey().ids()
                                    .stream()
                                    .map(BrawlStarsId::new)
                                    .toList(),
                            new RankCount(rankToCount)
                    );
                }).toList();
    }

}
