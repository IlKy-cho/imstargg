package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsEntity;
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
public class BrawlerResultStatisticsRepository {

    private static final int PAGE_SIZE = 1000;

    private final BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository;
    private final BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository;
    private final BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository;

    public BrawlerResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository,
            BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlersBattleResultStatisticsJpaRepository = brawlersBattleResultStatisticsJpaRepository;
        this.brawlerEnemyBattleResultStatisticsJpaRepository = brawlerEnemyBattleResultStatisticsJpaRepository;
    }

    public List<BrawlerResultCount> findBrawlerResultCounts(
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange
    ) {
        Map<BrawlStarsId, ResultCounter> brawlerIdToResultCounter = new HashMap<>();
        Map<BrawlStarsId, Counter> brawlerIdToStarPlayerCounter = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerBattleResultStatisticsEntity> slice = brawlerBattleResultStatisticsJpaRepository
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawlerFalse(
                            battleDate, trophyRange, soloRankTierRange, pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            slice.forEach(statsEntity -> {
                BrawlStarsId brawlerId = new BrawlStarsId(statsEntity.getBrawlerBrawlStarsId());
                ResultCounter resultCounter = brawlerIdToResultCounter.computeIfAbsent(
                        brawlerId, k -> new ResultCounter()
                );
                resultCounter.addVictory(statsEntity.getVictoryCount());
                resultCounter.addDefeat(statsEntity.getDefeatCount());
                resultCounter.addDraw(statsEntity.getDrawCount());

                Counter starPlayerCounter = brawlerIdToStarPlayerCounter.computeIfAbsent(
                        brawlerId, k -> new Counter()
                );
                starPlayerCounter.add(statsEntity.getStarPlayerCount());
            });
        }

        return brawlerIdToResultCounter.entrySet().stream()
                .map(entry -> new BrawlerResultCount(
                        entry.getKey(),
                        new ResultCount(
                                entry.getValue().getVictoryCount(),
                                entry.getValue().getDefeatCount(),
                                entry.getValue().getDrawCount()
                        ),
                        new StarPlayerCount(brawlerIdToStarPlayerCounter.get(entry.getKey()).getCount())
                )).toList();
    }

    public List<BattleEventResultCount> findBrawlerBattleEventResultCounts(
            BrawlStarsId brawlerId,
            LocalDate date,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {
        return brawlerBattleResultStatisticsJpaRepository
                .findAllByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndDuplicateBrawlerFalse(
                        date, trophyRange, soloRankTierRange, brawlerId.value()
                ).stream()
                .map(statsEntity -> new BattleEventResultCount(
                        new BrawlStarsId(statsEntity.getEventBrawlStarsId()),
                        new ResultCount(
                                statsEntity.getVictoryCount(),
                                statsEntity.getDefeatCount(),
                                statsEntity.getDrawCount()
                        ),
                        new StarPlayerCount(statsEntity.getStarPlayerCount())
                )).toList();
    }

    public List<BrawlersResultCount> findBrawlerBrawlersResultCounts(
            BrawlStarsId brawlerId,
            LocalDate date,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {
        Map<BrawlerIdHash, ResultCounter> brawlerIdHashToCounter = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlersBattleResultStatisticsEntity> slice = brawlersBattleResultStatisticsJpaRepository
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndDuplicateBrawlerFalse(
                            date,
                            trophyRange, soloRankTierRange,
                            brawlerId.value(),
                            pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            slice.forEach(statsEntity -> {
                BrawlerIdHash brawlerIdHash = new BrawlerIdHash(statsEntity.getBrawlers().getIdHash());
                ResultCounter counter = brawlerIdHashToCounter.computeIfAbsent(brawlerIdHash, k -> new ResultCounter());
                counter.addVictory(statsEntity.getVictoryCount());
                counter.addDefeat(statsEntity.getDefeatCount());
                counter.addDraw(statsEntity.getDrawCount());
            });
        }

        return brawlerIdHashToCounter.entrySet().stream()
                .map(entry -> new BrawlersResultCount(
                        entry.getKey().ids().stream().map(BrawlStarsId::new).toList(),
                        new ResultCount(
                                entry.getValue().getVictoryCount(),
                                entry.getValue().getDefeatCount(),
                                entry.getValue().getDrawCount()
                        )
                )).toList();
    }

    public List<BrawlerEnemyResultCount> findBrawlerEnemyResultCounts(
            BrawlStarsId brawlerId,
            LocalDate date,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {
        Map<BrawlStarsId, ResultCounter> enemyBrawlerIdToResultCounter = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlerEnemyBattleResultStatisticsEntity> slice = brawlerEnemyBattleResultStatisticsJpaRepository
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndDuplicateBrawlerFalse(
                            date, trophyRange, soloRankTierRange, brawlerId.value(), pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            slice.forEach(statsEntity -> {
                BrawlStarsId enemyBrawlerId = new BrawlStarsId(statsEntity.getEnemyBrawlerBrawlStarsId());
                ResultCounter resultCounter = enemyBrawlerIdToResultCounter.computeIfAbsent(
                        enemyBrawlerId, k -> new ResultCounter()
                );
                resultCounter.addVictory(statsEntity.getVictoryCount());
                resultCounter.addDefeat(statsEntity.getDefeatCount());
                resultCounter.addDraw(statsEntity.getDrawCount());
            });
        }

        return enemyBrawlerIdToResultCounter.entrySet().stream()
                .map(entry -> new BrawlerEnemyResultCount(
                        brawlerId,
                        entry.getKey(),
                        new ResultCount(
                                entry.getValue().getVictoryCount(),
                                entry.getValue().getDefeatCount(),
                                entry.getValue().getDrawCount()
                        )
                )).toList();
    }

}
