package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsJpaRepository;
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
    private final BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository;

    public BattleEventResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository,
            BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository
    ) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlersBattleResultStatisticsJpaRepository = brawlersBattleResultStatisticsJpaRepository;
        this.brawlerEnemyBattleResultStatisticsJpaRepository = brawlerEnemyBattleResultStatisticsJpaRepository;
    }

    public List<BrawlerResultCount> findBrawlerResultCounts(
            BrawlStarsId eventId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        return brawlerBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawler(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange,
                        duplicateBrawler
                ).stream()
                .map(statsEntity -> new BrawlerResultCount(
                        new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId()),
                        new ResultCount(
                                statsEntity.getVictoryCount(),
                                statsEntity.getDefeatCount(),
                                statsEntity.getDrawCount()
                        ),
                        new StarPlayerCount(statsEntity.getStarPlayerCount())
                )).toList();
    }

    public List<BrawlersResultCount> findBrawlersResultCounts(
            BrawlStarsId eventId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            int brawlNum, boolean duplicateBrawler
    ) {
        Map<BrawlerIdHash, ResultCounter> brawlersCounters = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;

        while (hasNext) {
            Slice<BrawlersBattleResultStatisticsEntity> brawlersBattleResultStatsSlice = brawlersBattleResultStatisticsJpaRepository
                    .findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlersNumAndDuplicateBrawler(
                            eventId.value(), battleDate, trophyRange, soloRankTierRange,
                            brawlNum, duplicateBrawler,
                            pageRequest
                    );

            hasNext = brawlersBattleResultStatsSlice.hasNext();

            brawlersBattleResultStatsSlice.forEach(stats -> {
                ResultCounter brawlersCounter = brawlersCounters.computeIfAbsent(
                        new BrawlerIdHash(stats.getBrawlers().getIdHash()),
                        k -> new ResultCounter()
                );
                brawlersCounter.addVictory(stats.getVictoryCount());
                brawlersCounter.addDefeat(stats.getDefeatCount());
                brawlersCounter.addDraw(stats.getDrawCount());
            });

            pageRequest = pageRequest.next();
        }

        return brawlersCounters.entrySet().stream()
                .map(entry -> new BrawlersResultCount(
                        entry.getKey().ids().stream().map(BrawlStarsId::new).toList(),
                        new ResultCount(
                                entry.getValue().getVictoryCount() / brawlNum,
                                entry.getValue().getDefeatCount() / brawlNum,
                                entry.getValue().getDrawCount() / brawlNum
                        )
                ))
                .toList();
    }

    public List<BrawlerEnemyResultCount> findBrawlerEnemyResultCounts(
            BrawlStarsId eventId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        return brawlerEnemyBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawler(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange,
                        duplicateBrawler
                ).stream()
                .map(statsEntity -> new BrawlerEnemyResultCount(
                        new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId()),
                        new BrawlStarsId(statsEntity.getEnemyBrawlerBrawlStarsId()),
                        new ResultCount(
                                statsEntity.getVictoryCount(),
                                statsEntity.getDefeatCount(),
                                statsEntity.getDrawCount()
                        )
                )).toList();
    }

}
