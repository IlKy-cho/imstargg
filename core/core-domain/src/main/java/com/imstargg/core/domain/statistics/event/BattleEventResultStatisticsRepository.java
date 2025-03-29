package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerPairResultCount;
import com.imstargg.core.domain.statistics.BrawlerResultCount;
import com.imstargg.core.domain.statistics.ResultCount;
import com.imstargg.core.domain.statistics.StarPlayerCount;
import com.imstargg.core.domain.statistics.TierRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BattleEventResultStatisticsRepository {

    private static final int PAGE_SIZE = 1000;

    private final BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository;
    private final BrawlerPairBattleResultStatisticsJpaRepository brawlerPairBattleResultStatisticsJpaRepository;
    private final BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository;

    public BattleEventResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlerPairBattleResultStatisticsJpaRepository brawlerPairBattleResultStatisticsJpaRepository,
            BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository
    ) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlerPairBattleResultStatisticsJpaRepository = brawlerPairBattleResultStatisticsJpaRepository;
        this.brawlerEnemyBattleResultStatisticsJpaRepository = brawlerEnemyBattleResultStatisticsJpaRepository;
    }

    public List<BrawlerResultCount> findBrawlerResultCounts(
            BrawlStarsId eventId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> brawlerIdToResultCount = new HashMap<>();
        Map<BrawlStarsId, StarPlayerCount> brawlerIdToStarPlayerCount = new HashMap<>();
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerBattleResultStatisticsEntity> statsEntitySlice = brawlerBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            eventId.value(), tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId brawlerId = new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId());
                ResultCount resultCount = new ResultCount(
                        statsEntity.getVictoryCount(),
                        statsEntity.getDefeatCount(),
                        statsEntity.getDrawCount()
                );
                StarPlayerCount starPlayerCount = new StarPlayerCount(statsEntity.getStarPlayerCount());
                brawlerIdToResultCount.merge(brawlerId, resultCount, ResultCount::merge);
                brawlerIdToStarPlayerCount.merge(brawlerId, starPlayerCount, StarPlayerCount::merge);
            });
        }

        return brawlerIdToResultCount.entrySet().stream()
                .map(entry -> new BrawlerResultCount(
                        entry.getKey(),
                        entry.getValue(),
                        brawlerIdToStarPlayerCount.get(entry.getKey()))
                ).toList();
    }

    public List<BrawlerPairResultCount> findBrawlerPairResultCounts(
            BrawlStarsId eventId,
            BrawlStarsId brawlerId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> pairBrawlerIdToResultCount = new HashMap<>();
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerPairBattleResultStatisticsEntity> statsEntitySlice = brawlerPairBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            eventId.value(), brawlerId.value(), tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId pairBrawlerId = new BrawlStarsId(statsEntity.getPairBrawlerBrawlStarsId());
                ResultCount resultCount = new ResultCount(
                        statsEntity.getVictoryCount(),
                        statsEntity.getDefeatCount(),
                        statsEntity.getDrawCount()
                );
                pairBrawlerIdToResultCount.merge(pairBrawlerId, resultCount, ResultCount::merge);
            });
        }

        return pairBrawlerIdToResultCount.entrySet().stream()
                .map(entry -> new BrawlerPairResultCount(brawlerId, entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<BrawlerPairResultCount> findBrawlerEnemyResultCounts(
            BrawlStarsId eventId,
            BrawlStarsId brawlerId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> enemyBrawlerIdToResultCount = new HashMap<>();
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerEnemyBattleResultStatisticsEntity> statsEntitySlice = brawlerEnemyBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            eventId.value(), brawlerId.value(), tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId enemyBrawlerId = new BrawlStarsId(statsEntity.getEnemyBrawlerBrawlStarsId());
                ResultCount resultCount = new ResultCount(
                        statsEntity.getVictoryCount(),
                        statsEntity.getDefeatCount(),
                        statsEntity.getDrawCount()
                );
                enemyBrawlerIdToResultCount.merge(enemyBrawlerId, resultCount, ResultCount::merge);
            });
        }

        return enemyBrawlerIdToResultCount.entrySet().stream()
                .map(entry -> new BrawlerPairResultCount(brawlerId, entry.getKey(), entry.getValue()))
                .toList();
    }

}
