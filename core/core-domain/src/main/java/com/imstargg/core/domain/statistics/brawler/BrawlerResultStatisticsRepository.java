package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.domain.brawlstars.BattleEventRepositoryWithCache;
import com.imstargg.core.domain.statistics.BattleEventResultCount;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultCount;
import com.imstargg.core.domain.statistics.BrawlerResultCount;
import com.imstargg.core.domain.statistics.BrawlersResultCount;
import com.imstargg.core.domain.statistics.Counter;
import com.imstargg.core.domain.statistics.ResultCount;
import com.imstargg.core.domain.statistics.ResultCounter;
import com.imstargg.core.domain.statistics.StarPlayerCount;
import com.imstargg.core.enums.Language;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.IdHash;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BrawlerResultStatisticsRepository {

    private static final int PAGE_SIZE = 1000;

    private final BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository;
    private final BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository;
    private final BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository;
    private final BattleEventRepositoryWithCache battleEventRepository;

    public BrawlerResultStatisticsRepository(
            BrawlerBattleResultStatisticsJpaRepository brawlerBattleResultStatisticsJpaRepository,
            BrawlersBattleResultStatisticsJpaRepository brawlersBattleResultStatisticsJpaRepository,
            BrawlerEnemyBattleResultStatisticsJpaRepository brawlerEnemyBattleResultStatisticsJpaRepository,
            BattleEventRepositoryWithCache battleEventRepository
    ) {
        this.brawlerBattleResultStatisticsJpaRepository = brawlerBattleResultStatisticsJpaRepository;
        this.brawlersBattleResultStatisticsJpaRepository = brawlersBattleResultStatisticsJpaRepository;
        this.brawlerEnemyBattleResultStatisticsJpaRepository = brawlerEnemyBattleResultStatisticsJpaRepository;
        this.battleEventRepository = battleEventRepository;
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
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRange(
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
        List<BrawlerBattleResultStatisticsEntity> eventResultStatsEntities = brawlerBattleResultStatisticsJpaRepository
                .findAllByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsId(
                        date, trophyRange, soloRankTierRange, brawlerId.value()
                );
        Map<BrawlStarsId, BattleEvent> idToEvent = battleEventRepository.findAllEvents(
                eventResultStatsEntities.stream()
                        .map(statsEntity -> new BrawlStarsId(statsEntity.getPassNumber()))
                        .toList(), Language.KOREAN
        ).stream().collect(Collectors.toMap(BattleEvent::id, Function.identity()));

        return eventResultStatsEntities.stream()
                .filter(statsEntity -> idToEvent.containsKey(new BrawlStarsId(statsEntity.getPassNumber())))
                .map(statsEntity -> new BattleEventResultCount(
                        idToEvent.get(new BrawlStarsId(statsEntity.getPassNumber())),
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
            @Nullable SoloRankTierRange soloRankTierRange,
            int brawlersNum
    ) {
        Map<IdHash, ResultCounter> brawlerIdHashToCounter = new HashMap<>();
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        boolean hasNext = true;
        while (hasNext) {
            Slice<BrawlersBattleResultStatisticsEntity> slice = brawlersBattleResultStatisticsJpaRepository
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
                            date,
                            trophyRange, soloRankTierRange,
                            brawlerId.value(),
                            brawlersNum,
                            pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            slice.forEach(statsEntity -> {
                IdHash brawlerIdHash = new IdHash(statsEntity.getBrawlers().getIdHash());
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
                    .findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsId(
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
