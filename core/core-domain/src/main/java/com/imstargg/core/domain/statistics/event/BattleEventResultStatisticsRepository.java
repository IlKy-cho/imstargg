package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultCount;
import com.imstargg.core.domain.statistics.BrawlerResultCount;
import com.imstargg.core.domain.statistics.BrawlersResultCount;
import com.imstargg.core.domain.statistics.ResultCount;
import com.imstargg.core.domain.statistics.StarPlayerCount;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsJpaRepository;
import com.imstargg.storage.db.core.statistics.IdHash;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange
    ) {
        return brawlerBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRange(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange
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
            BrawlStarsId eventId,
            BrawlStarsId brawlerId,
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            int brawlNum
    ) {
        return brawlersBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange, brawlerId.value(), brawlNum
                ).stream()
                .map(statsEntity -> new BrawlersResultCount(
                        new IdHash(statsEntity.getBrawlers().getIdHash()).ids().stream()
                                .map(BrawlStarsId::new).toList(),
                        new ResultCount(
                                statsEntity.getVictoryCount(),
                                statsEntity.getDefeatCount(),
                                statsEntity.getDrawCount()
                        )
                )).toList();
    }

    public List<BrawlerEnemyResultCount> findBrawlerEnemyResultCounts(
            BrawlStarsId eventId,
            BrawlStarsId brawlerId,
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange
    ) {
        return brawlerEnemyBattleResultStatisticsJpaRepository
                .findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsId(
                        eventId.value(), battleDate, trophyRange, soloRankTierRange, brawlerId.value()
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
