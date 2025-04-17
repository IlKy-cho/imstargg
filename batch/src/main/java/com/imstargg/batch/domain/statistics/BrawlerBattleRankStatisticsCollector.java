package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;


public class BrawlerBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final LocalDate battleDate;
    private final ConcurrentMap<Key, BrawlerBattleRankStatisticsCollectionEntity> cache;
    private final ConcurrentMap<Key, BrawlerBattleRankStatisticsCollectionEntity> statsToSave = new ConcurrentHashMap<>();

    public BrawlerBattleRankStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            LocalDate battleDate,
            List<BrawlerBattleRankStatisticsCollectionEntity> statsEntities
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

        Key.of(battle).forEach(key ->
                getStats(key).countUp(
                        Objects.requireNonNull(battle.getPlayer().getRank())
                )
        );
        return true;
    }

    private BrawlerBattleRankStatisticsCollectionEntity getStats(Key key) {
        var stats = cache.computeIfAbsent(key, k1 -> new BrawlerBattleRankStatisticsCollectionEntity(
                key.eventBrawlStarsId(),
                key.brawlerBrawlStarsId(),
                key.trophyRange(),
                this.battleDate
        ));
        statsToSave.put(key, stats);
        return stats;
    }

    @Override
    public List<BrawlerBattleRankStatisticsCollectionEntity> getStatistics() {
        return statsToSave.values().stream().toList();
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange
    ) {

        static Key of(BrawlerBattleRankStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    TrophyRange.valueOf(statsEntity.getTierRange())
            );
        }

        static List<Key> of(BattleCollectionEntity battle) {
            return battle.findMe().stream()
                    .flatMap(myPlayer ->
                            TrophyRange.findAll(myPlayer.getBrawler().getTrophies()).stream()
                                    .map(trophyRange -> new Key(
                                            Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                                            myPlayer.getBrawler().getBrawlStarsId(),
                                            trophyRange
                                    ))
                    ).toList();
        }
    }
}
