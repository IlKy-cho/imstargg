package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.domain.brawlstars.BattleEventRepositoryWithCache;
import com.imstargg.core.domain.statistics.BattleEventResultCount;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BrawlerResultStatisticsRepository {

    private static final int PAGE_SIZE = 1000;

    private final BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository;
    private final BrawlerPairBattleResultStatisticsJpaRepository brawlerPairBattleResultStatisticsJpaRepository;
    private final BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository;
    private final BattleEventRepositoryWithCache battleEventRepository;

    public BrawlerResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlerPairBattleResultStatisticsJpaRepository brawlerPairBattleResultStatisticsJpaRepository,
            BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository,
            BattleEventRepositoryWithCache battleEventRepository
    ) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlerPairBattleResultStatisticsJpaRepository = brawlerPairBattleResultStatisticsJpaRepository;
        this.brawlerEnemyBattleResultStatisticsJpaRepository = brawlerEnemyBattleResultStatisticsJpaRepository;
        this.battleEventRepository = battleEventRepository;
    }

    public List<BrawlerResultCount> findBrawlerResultCounts(
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> brawlerIdToResultCount = new HashMap<>();
        Map<BrawlStarsId, StarPlayerCount> brawlerIdToStarPlayerCount = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerBattleResultStatisticsEntity> slice = brawlerBattleResultStatisticsJpaRepository
                    .findSliceByTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            slice.forEach(statsEntity -> {
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
                        brawlerIdToStarPlayerCount.get(entry.getKey())
                )).toList();
    }

    public List<BattleEventResultCount> findBrawlerBattleEventResultCounts(
            BrawlStarsId brawlerId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> eventIdToResultCount = new HashMap<>();
        Map<BrawlStarsId, StarPlayerCount> eventIdToStarPlayerCount = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerBattleResultStatisticsEntity> statsEntitySlice = brawlerBattleResultStatisticsJpaRepository
                    .findSliceByBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            brawlerId.value(), tierRange.value(), startDate, endDate, PageRequest.ofSize(PAGE_SIZE)
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                BrawlStarsId eventId = new BrawlStarsId(statsEntity.getEventBrawlStarsId());
                ResultCount resultCount = new ResultCount(
                        statsEntity.getVictoryCount(),
                        statsEntity.getDefeatCount(),
                        statsEntity.getDrawCount()
                );
                StarPlayerCount starPlayerCount = new StarPlayerCount(statsEntity.getStarPlayerCount());
                eventIdToResultCount.merge(eventId, resultCount, ResultCount::merge);
                eventIdToStarPlayerCount.merge(eventId, starPlayerCount, StarPlayerCount::merge);
            });
        }

        Map<BrawlStarsId, BattleEvent> idToEvent = battleEventRepository.findAllEvents(
                eventIdToStarPlayerCount.keySet()
        ).stream().collect(Collectors.toMap(BattleEvent::id, Function.identity()));

        return eventIdToResultCount.entrySet().stream()
                .map(entry -> new BattleEventResultCount(
                        idToEvent.get(entry.getKey()),
                        entry.getValue(),
                        eventIdToStarPlayerCount.get(entry.getKey())
                )).toList();
    }

    public List<BrawlerPairResultCount> findBrawlerPairResultCounts(
            BrawlStarsId brawlerId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> pairBrawlerIdToResultCount = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerPairBattleResultStatisticsEntity> statsEntitySlice = brawlerPairBattleResultStatisticsJpaRepository
                    .findSliceByBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            brawlerId.value(), tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                var pairBrawlerId = new BrawlStarsId(statsEntity.getPairBrawlerBrawlStarsId());
                var resultCount = new ResultCount(
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
            BrawlStarsId brawlerId,
            TierRange tierRange,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<BrawlStarsId, ResultCount> enemyBrawlerIdToResultCount = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerEnemyBattleResultStatisticsEntity> statsEntitySlice = brawlerEnemyBattleResultStatisticsJpaRepository
                    .findSliceByBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
                            brawlerId.value(), tierRange.value(), startDate, endDate, pageRequest
                    );
            hasNext = statsEntitySlice.hasNext();
            pageRequest = pageRequest.next();

            statsEntitySlice.forEach(statsEntity -> {
                var enemyBrawlerId = new BrawlStarsId(statsEntity.getEnemyBrawlerBrawlStarsId());
                var resultCount = new ResultCount(
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
