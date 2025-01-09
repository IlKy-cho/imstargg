package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsEntity;
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
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTier soloRankTier,
            boolean duplicateBrawler
    ) {
        Map<Long, BattleEventBrawlerCounter> brawlerCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {
            Slice<BrawlerBattleResultStatisticsEntity> brawlerBattleResultStatsSlice = brawlerBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawler(
                            eventBrawlStarsId, battleDate, trophyRange, soloRankTier,
                            duplicateBrawler,
                            pageRequest
                    );
            hasNext = brawlerBattleResultStatsSlice.hasNext();

            brawlerBattleResultStatsSlice.forEach(stats -> {
                BattleEventBrawlerCounter brawlerCounter = brawlerCounters.computeIfAbsent(
                        stats.getBrawlerBrawlStarsId(),
                        k -> new BattleEventBrawlerCounter()
                );
                brawlerCounter.addVictory(stats.getVictoryCount());
                brawlerCounter.addDefeat(stats.getDefeatCount());
                brawlerCounter.addDraw(stats.getDrawCount());
                brawlerCounter.addStarPlayer(stats.getStarPlayerCount());
            });

            pageRequest = pageRequest.next();
        }

        return brawlerCounters.entrySet().stream()
                .map(entry -> new BattleEventBrawlerResultCount(
                        new BrawlStarsId(entry.getKey()),
                        entry.getValue().getVictoryCount(),
                        entry.getValue().getDefeatCount(),
                        entry.getValue().getDrawCount(),
                        entry.getValue().getStarPlayerCount()
                ))
                .toList();
    }

    public List<BattleEventBrawlersResultStatistics> findBrawlersResultStatistics(
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTier soloRankTier,
            int brawlNum, boolean duplicateBrawler
    ) {
        Map<BrawlerIdHash, BattleEventBrawlersCounter> brawlersCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {
            Slice<BrawlersBattleResultStatisticsEntity> brawlersBattleResultStatsSlice = brawlersBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlersNumAndDuplicateBrawler(
                            eventBrawlStarsId, battleDate, trophyRange, soloRankTier,
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

    private static class BattleEventBrawlerCounter {

        private long victoryCount;
        private long defeatCount;
        private long drawCount;
        private long starPlayerCount;

        BattleEventBrawlerCounter() {
            this.victoryCount = 0;
            this.defeatCount = 0;
            this.drawCount = 0;
            this.starPlayerCount = 0;
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

        void addStarPlayer(long count) {
            starPlayerCount += count;
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

        long getStarPlayerCount() {
            return starPlayerCount;
        }
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
