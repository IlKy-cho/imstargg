package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BrawlerEnemyBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final LocalDate battleDate;
    private final ConcurrentMap<Key, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentMap<Key, BrawlerEnemyBattleResultStatisticsCollectionEntity> statsToSave = new ConcurrentHashMap<>();

    public BrawlerEnemyBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            LocalDate battleDate,
            List<BrawlerEnemyBattleResultStatisticsCollectionEntity> statsEntities
    ) {
        this.validator = validator;
        this.battleDate = battleDate;
        this.cache = new ConcurrentHashMap<>(statsEntities.stream()
                .collect(Collectors.toMap(Key::of, Function.identity())));
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!validator.validateResultStatisticsCollection(battle)) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        new BattleCombinationBuilder(battle).pairWithOpponents().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getBrawlerEnemyBattleResultStats(key).countUp(battleResult)
                )
        );
        return true;
    }

    private BrawlerEnemyBattleResultStatisticsCollectionEntity getBrawlerEnemyBattleResultStats(Key key) {
        var stats = cache.computeIfAbsent(key, k1 -> Optional.ofNullable(key.trophyRange())
                .map(trophyRange -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        trophyRange,
                        this.battleDate,
                        key.enemyBrawlerBrawlStarsId()
                )).or(() -> Optional.ofNullable(key.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                                key.eventBrawlStarsId(),
                                key.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                this.battleDate,
                                key.enemyBrawlerBrawlStarsId()
                        )))
                .orElseGet(() -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        this.battleDate,
                        key.enemyBrawlerBrawlStarsId()
                ))
        );
        statsToSave.put(key, stats);
        return stats;
    }

    @Override
    public List<BrawlerEnemyBattleResultStatisticsCollectionEntity> getStatistics() {
        return statsToSave.values().stream().toList();
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            long enemyBrawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {

        static Key of(BrawlerEnemyBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    statsEntity.getEnemyBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null)
            );
        }

        static List<Key> of(
                BattleCollectionEntity battle,
                BattleCollectionEntityTeamPlayer myPlayer,
                BattleCollectionEntityTeamPlayer enemyPlayer
        ) {
            return new KeyBuilder(battle, myPlayer).build((trophyRange, soloRankTierRange) -> new Key(
                    Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                    myPlayer.getBrawler().getBrawlStarsId(),
                    enemyPlayer.getBrawler().getBrawlStarsId(),
                    trophyRange,
                    soloRankTierRange
            ));
        }
    }
}