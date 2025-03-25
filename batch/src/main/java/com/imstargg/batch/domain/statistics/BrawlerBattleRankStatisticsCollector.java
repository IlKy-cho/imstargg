package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionJpaRepository;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class BrawlerBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerBattleRankStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerBattleRankStatisticsCollectionJpaRepository jpaRepository;
    private final ConcurrentMap<Key, BrawlerBattleRankStatisticsCollectionEntity> cache = new ConcurrentHashMap<>();

    public BrawlerBattleRankStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            BrawlerBattleRankStatisticsCollectionJpaRepository jpaRepository
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

    private BrawlerBattleRankStatisticsCollectionEntity getStats(
            Key key) {
        return cache.computeIfAbsent(key, k -> new BrawlerBattleRankStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.brawlerBrawlStarsId(),
                k.trophyRange(),
                k.battleDate()
        ));
    }

    @Override
    public void save() {
        jpaRepository.saveAll(cache.values());
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {

        static Key of(BrawlerBattleRankStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    TrophyRange.valueOf(statsEntity.getTierRange()),
                    statsEntity.getBattleDate()
            );
        }

        static List<Key> of(BattleCollectionEntity battle) {
            return battle.findMe().stream()
                    .flatMap(myPlayer ->
                            TrophyRange.findAll(myPlayer.getBrawler().getTrophies()).stream()
                                    .map(trophyRange -> new Key(
                                            Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                                            myPlayer.getBrawler().getBrawlStarsId(),
                                            trophyRange,
                                            battle.getBattleTime().toLocalDate()
                                    ))
                    ).toList();
        }
    }
}
