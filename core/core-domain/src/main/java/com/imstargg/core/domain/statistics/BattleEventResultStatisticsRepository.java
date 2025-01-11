package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsJpaRepository;
import jakarta.annotation.Nullable;
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
    private final BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository;

    public BattleEventResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository
    ) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlersBattleResultStatisticsJpaRepository = brawlersBattleResultStatisticsJpaRepository;
    }

    public List<BattleEventBrawlerResultCount> findBrawlerResultCounts(
            BrawlStarsId eventId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        return brawlerBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawler(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange,
                        duplicateBrawler
                ).stream()
                .map(statsEntity -> new BattleEventBrawlerResultCount(
                        new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId()),
                        statsEntity.getVictoryCount(),
                        statsEntity.getDefeatCount(),
                        statsEntity.getDrawCount(),
                        statsEntity.getStarPlayerCount()
                )).toList();
    }

    public List<BattleEventBrawlersResultStatistics> findBrawlersResultStatistics(
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            int brawlNum, boolean duplicateBrawler
    ) {
        Map<BrawlerIdHash, BattleEventBrawlersCounter> brawlersCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {
            Slice<BrawlersBattleResultStatisticsEntity> brawlersBattleResultStatsSlice = brawlersBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlersNumAndDuplicateBrawler(
                            eventBrawlStarsId, battleDate, trophyRange, soloRankTierRange,
                            brawlNum, duplicateBrawler,
                            pageRequest
                    );

            hasNext = brawlersBattleResultStatsSlice.hasNext();

            brawlersBattleResultStatsSlice.forEach(stats -> {
                BattleEventBrawlersCounter brawlersCounter = brawlersCounters.computeIfAbsent(
                        new BrawlerIdHash(stats.getBrawlers().getIdHash()),
                        k -> new BattleEventBrawlersCounter()
                );
                brawlersCounter.addVictory(stats.getVictoryCount());
                brawlersCounter.addDefeat(stats.getDefeatCount());
                brawlersCounter.addDraw(stats.getDrawCount());
            });

            pageRequest = pageRequest.next();
        }

        return brawlersCounters.entrySet().stream()
                .map(entry -> new BattleEventBrawlersResultStatistics(
                        entry.getKey().ids().stream().map(BrawlStarsId::new).toList(),
                        entry.getValue().getVictoryCount() / entry.getKey().num(),
                        entry.getValue().getDefeatCount() / entry.getKey().num(),
                        entry.getValue().getDrawCount() / entry.getKey().num()
                ))
                .toList();
    }


    private static class BattleEventBrawlersCounter {

        private long victoryCount;
        private long defeatCount;
        private long drawCount;

        BattleEventBrawlersCounter() {
            this.victoryCount = 0;
            this.defeatCount = 0;
            this.drawCount = 0;
        }

        void addVictory(long count) {
            victoryCount += count;
        }

        void addDefeat(long count) {
            defeatCount += count;
        }

        void addDraw(long count) {
            drawCount += count;
        }


        long getVictoryCount() {
            return victoryCount;
        }

        long getDefeatCount() {
            return defeatCount;
        }

        long getDrawCount() {
            return drawCount;
        }

    }
}
