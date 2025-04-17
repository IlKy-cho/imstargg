package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BrawlerBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final LocalDate battleDate;
    private final ConcurrentMap<Key, BrawlerBattleResultStatisticsCollectionEntity> cache;
    private final ConcurrentMap<Key, BrawlerBattleResultStatisticsCollectionEntity> statsToSave = new ConcurrentHashMap<>();

    public BrawlerBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            LocalDate battleDate,
            List<BrawlerBattleResultStatisticsCollectionEntity> statsEntities
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
        var stats = cache.computeIfAbsent(key, k1 -> Optional.ofNullable(key.trophyRange())
                .map(trophyRange -> new BrawlerBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        trophyRange,
                        this.battleDate
                )).or(() -> Optional.ofNullable(key.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerBattleResultStatisticsCollectionEntity(
                                key.eventBrawlStarsId(),
                                key.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                this.battleDate
                        )))
                .orElseGet(() -> new BrawlerBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        this.battleDate
                ))
        );
        statsToSave.put(key, stats);
        return stats;
    }

    @Override
    public List<BrawlerBattleResultStatisticsCollectionEntity> getStatistics() {
        return cache.values().stream().toList();
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {
        static Key of(BrawlerBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null)
            );
        }

        static List<Key> of(BattleCollectionEntity battle, BattleCollectionEntityTeamPlayer myPlayer) {
            return new KeyBuilder(battle, myPlayer).build((trophyRange, soloRankTierRange) -> new Key(
                    Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                    myPlayer.getBrawler().getBrawlStarsId(),
                    trophyRange,
                    soloRankTierRange
            ));
        }
    }
}