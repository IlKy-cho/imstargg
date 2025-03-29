package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleRankStatisticsCollectionJpaRepository;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrawlerPairBattleRankStatisticsCollector
        implements StatisticsCollector<BrawlerPairBattleRankStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerPairBattleRankStatisticsCollectionJpaRepository jpaRepository;
    private final ConcurrentMap<Key, BrawlerPairBattleRankStatisticsCollectionEntity> cache = new ConcurrentHashMap<>();

    public BrawlerPairBattleRankStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            BrawlerPairBattleRankStatisticsCollectionJpaRepository jpaRepository
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

        new BattleCombinationBuilder(battle).pairWithTeam().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getStats(key).countUp(Objects.requireNonNull(battle.getPlayer().getRank()))
                )
        );
        return true;
    }

    @Override
    public void save() {
        jpaRepository.saveAll(cache.values());
    }

    private BrawlerPairBattleRankStatisticsCollectionEntity getStats(Key key) {
        return cache.computeIfAbsent(key, k -> new BrawlerPairBattleRankStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.brawlerBrawlStarsId(),
                k.trophyRange(),
                k.battleDate(),
                k.pairBrawlerBrawlStarsId()
        ));
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            long pairBrawlerBrawlStarsId,
            TrophyRange trophyRange,
            LocalDate battleDate
    ) {

        static Key of(BrawlerPairBattleRankStatisticsCollectionEntity entity) {
            return new Key(
                    entity.getEventBrawlStarsId(),
                    entity.getBrawlerBrawlStarsId(),
                    entity.getPairBrawlerBrawlStarsId(),
                    TrophyRange.valueOf(entity.getTierRange()),
                    entity.getBattleDate()
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
                            trophyRange,
                            battle.getBattleTime().toLocalDate()
                    )).toList();
        }
    }
}
