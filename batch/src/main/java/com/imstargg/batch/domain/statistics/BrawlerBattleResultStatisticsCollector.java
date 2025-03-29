package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionJpaRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * - duel 의 경우 플레이어 수가 1개 보다 많기 때문에 플레이어가 1명인 것에 대한 예외처리는 하지 않음
 */
public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerBattleResultStatisticsCollectionJpaRepository jpaRepository;
    private final ConcurrentMap<Key, BrawlerBattleResultStatisticsCollectionEntity> cache = new ConcurrentHashMap<>();

    public BrawlerBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            BrawlerBattleResultStatisticsCollectionJpaRepository jpaRepository
    ) {
        this.validator = validator;
        this.jpaRepository = jpaRepository;
    }

    @PostConstruct
    void init() {
        jpaRepository.findAllByBattleDateGreaterThanEqual(validator.getMinCollectableBattleDate())
                .forEach(entity -> cache.put(Key.of(entity), entity));
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!validator.validateResultStatisticsCollection(battle)) {
            return false;
        }

        for (BattleCollectionEntityTeamPlayer player : battle.findMe()) {
            BattleResult battleResult = BattleResult.map(battle.getResult());
            Key.of(battle, player).forEach(key -> {
                var brawlerBattleResultStats = getStats(key);
                brawlerBattleResultStats.countUp(battleResult);
                if (battle.isStarPlayer(player)) {
                    brawlerBattleResultStats.starPlayer();
                }
            });
        }

        return true;
    }

    private BrawlerBattleResultStatisticsCollectionEntity getStats(Key key) {
        return cache.computeIfAbsent(key, k -> Optional.ofNullable(k.trophyRange)
                .map(trophyRange -> new BrawlerBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        trophyRange,
                        k.battleDate()
                )).or(() -> Optional.ofNullable(k.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerBattleResultStatisticsCollectionEntity(
                                k.eventBrawlStarsId(),
                                k.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                k.battleDate()
                        )))
                .orElseGet(() -> new BrawlerBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        k.battleDate()
                ))
        );
    }

    @Override
    public void save() {
        jpaRepository.saveAll(cache.values());
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {
        static Key of(BrawlerBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null),
                    statsEntity.getBattleDate()
            );
        }

        static List<Key> of(BattleCollectionEntity battle, BattleCollectionEntityTeamPlayer myPlayer) {
            return new KeyBuilder(battle, myPlayer).build((trophyRange, soloRankTierRange) -> new Key(
                    Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                    myPlayer.getBrawler().getBrawlStarsId(),
                    trophyRange,
                    soloRankTierRange,
                    battle.getBattleTime().toLocalDate()
            ));
        }
    }
}
