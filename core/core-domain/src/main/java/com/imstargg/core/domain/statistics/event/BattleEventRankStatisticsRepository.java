package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerRankCount;
import com.imstargg.core.domain.statistics.BrawlersRankCount;
import com.imstargg.core.domain.statistics.RankCount;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.IdHash;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
            BrawlStarsId eventId, LocalDate battleDate, TrophyRange trophyRange, BrawlStarsId brawlerId, int brawlersNum
    ) {
        return brawlersBattleRankStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
                        eventId.value(), battleDate, trophyRange, brawlerId.value(), brawlersNum
                ).stream()
                .map(statsEntity -> new BrawlersRankCount(
                        new IdHash(statsEntity.getBrawlers().getIdHash()).ids().stream()
                                .map(BrawlStarsId::new).toList(),
                        new RankCount(statsEntity.getRankToCounts())
                )).toList();
    }

}
