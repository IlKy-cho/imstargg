package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BrawlerPairBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerPairBattleRankStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final LocalDate battleDate;
    private final ConcurrentMap<Key, BrawlerPairBattleRankStatisticsCollectionEntity> cache;
    private final ConcurrentMap<Key, BrawlerPairBattleRankStatisticsCollectionEntity> statsToSave = new ConcurrentHashMap<>();

    public BrawlerPairBattleRankStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            LocalDate battleDate,
            List<BrawlerPairBattleRankStatisticsCollectionEntity> statsEntities
    ) {
        this.validator = validator;
        this.battleDate = battleDate;
        this.cache = new ConcurrentHashMap<>(statsEntities.stream()
                .collect(Collectors.toMap(Key::of, Function.identity())));
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!validator.validateRankStatisticsCollection(battle)) {
            return false;
        }

        new BattleCombinationBuilder(battle).pairWithTeam().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getStats(key).countUp(Objects.requireNonNull(battle.getPlayer().getRank()))
                )
        );
        return true;
    }

    private BrawlerPairBattleRankStatisticsCollectionEntity getStats(Key key) {
        var stats = cache.computeIfAbsent(key, k1 -> new BrawlerPairBattleRankStatisticsCollectionEntity(
                key.eventBrawlStarsId(),
                key.brawlerBrawlStarsId(),
                key.trophyRange(),
                this.battleDate,
                key.pairBrawlerBrawlStarsId()
        ));
        statsToSave.put(key, stats);
        return stats;
    }

    @Override
    public List<BrawlerPairBattleRankStatisticsCollectionEntity> getStatistics() {
        return statsToSave.values().stream().toList();
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            long pairBrawlerBrawlStarsId,
            TrophyRange trophyRange
    ) {

        static Key of(BrawlerPairBattleRankStatisticsCollectionEntity entity) {
            return new Key(
                    entity.getEventBrawlStarsId(),
                    entity.getBrawlerBrawlStarsId(),
                    entity.getPairBrawlerBrawlStarsId(),
                    TrophyRange.valueOf(entity.getTierRange())
            );
        }

        static List<Key> of(
                BattleCollectionEntity battle,
                BattleCollectionEntityTeamPlayer myPlayer,
                BattleCollectionEntityTeamPlayer teamPlayer
        ) {
            return TrophyRange.findAll(myPlayer.getBrawler().getTrophies()).stream()
                    .map(trophyRange -> new Key(
                            Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                            myPlayer.getBrawler().getBrawlStarsId(),
                            teamPlayer.getBrawler().getBrawlStarsId(),
                            trophyRange
                    )).toList();
        }
    }
}