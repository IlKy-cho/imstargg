package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionEntity;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BrawlerPairBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerPairBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final LocalDate battleDate;
    private final ConcurrentMap<Key, BrawlerPairBattleResultStatisticsCollectionEntity> cache;

    public BrawlerPairBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            LocalDate battleDate,
            List<BrawlerPairBattleResultStatisticsCollectionEntity> statsEntities
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
        new BattleCombinationBuilder(battle).pairWithTeam().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getStats(key).countUp(battleResult)
                )
        );
        return true;
    }

    private BrawlerPairBattleResultStatisticsCollectionEntity getStats(Key key) {
        return cache.computeIfAbsent(key, k1 -> Optional.ofNullable(key.trophyRange())
                .map(trophyRange -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        trophyRange,
                        this.battleDate,
                        key.pairBrawlerBrawlStarsId()
                )).or(() -> Optional.ofNullable(key.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                                key.eventBrawlStarsId(),
                                key.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                this.battleDate,
                                key.pairBrawlerBrawlStarsId()
                        )))
                .orElseGet(() -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                        key.eventBrawlStarsId(),
                        key.brawlerBrawlStarsId(),
                        this.battleDate,
                        key.pairBrawlerBrawlStarsId()
                ))
        );
    }

    @Override
    public List<BrawlerPairBattleResultStatisticsCollectionEntity> getStatistics() {
        return cache.values().stream().toList();
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            long pairBrawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {

        static Key of(BrawlerPairBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    statsEntity.getPairBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null)
            );
        }

        static List<Key> of(
                BattleCollectionEntity battle,
                BattleCollectionEntityTeamPlayer myPlayer,
                BattleCollectionEntityTeamPlayer teamPlayer
        ) {
            return new KeyBuilder(battle, myPlayer).build((trophyRange, soloRankTierRange) -> new Key(
                    Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                    myPlayer.getBrawler().getBrawlStarsId(),
                    teamPlayer.getBrawler().getBrawlStarsId(),
                    trophyRange,
                    soloRankTierRange
            ));
        }
    }
}