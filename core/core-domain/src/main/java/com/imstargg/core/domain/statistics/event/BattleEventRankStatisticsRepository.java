package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerRankCount;
import com.imstargg.core.domain.statistics.BrawlerPairRankCount;
import com.imstargg.core.domain.statistics.RankCount;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsJpaRepository;
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
    private final BrawlerPairBattleRankStatisticsJpaRepository brawlerPairBattleRankStatisticsJpaRepository;

    public BattleEventRankStatisticsRepository(
            BrawlerBattleRankStatisticsJpaRepository brawlerBattleRankStatisticsJpaRepository,
            BrawlerPairBattleRankStatisticsJpaRepository brawlerPairBattleRankStatisticsJpaRepository
    ) {
        this.brawlerBattleRankStatisticsJpaRepository = brawlerBattleRankStatisticsJpaRepository;
        this.brawlerPairBattleRankStatisticsJpaRepository = brawlerPairBattleRankStatisticsJpaRepository;
    }

    public List<BrawlerRankCount> findBrawlerRankCounts(
            BrawlStarsId eventId,
            TrophyRange trophyRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);
        Map<BrawlStarsId, RankCount> brawlerIdToRankCount = new HashMap<>();
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerBattleRankStatisticsEntity> statsEntitySlice = brawlerBattleRankStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            eventId.value(), trophyRange.name(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId brawlerId = new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId());
                RankCount rankCount = new RankCount(statsEntity.getRankToCounts());
                brawlerIdToRankCount.merge(brawlerId, rankCount, RankCount::merge);
            });
        }

        return brawlerIdToRankCount.entrySet().stream()
                .map(entry -> new BrawlerRankCount(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<BrawlerPairRankCount> findBrawlerPairRankCounts(
            BrawlStarsId eventId,
            BrawlStarsId brawlerId,
            TrophyRange trophyRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);
        Map<BrawlStarsId, RankCount> pairBrawlerIdToRankCount = new HashMap<>();
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerPairBattleRankStatisticsEntity> statsEntitySlice = brawlerPairBattleRankStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            eventId.value(), brawlerId.value(), trophyRange.name(), startDate, endDate,
                            pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId pairBrawlerId = new BrawlStarsId(statsEntity.getPairBrawlerBrawlStarsId());
                RankCount rankCount = new RankCount(statsEntity.getRankToCounts());
                pairBrawlerIdToRankCount.merge(pairBrawlerId, rankCount, RankCount::merge);
            });
        }

        return pairBrawlerIdToRankCount.entrySet().stream()
                .map(entry -> new BrawlerPairRankCount(brawlerId, entry.getKey(), entry.getValue()))
                .toList();
    }

}
