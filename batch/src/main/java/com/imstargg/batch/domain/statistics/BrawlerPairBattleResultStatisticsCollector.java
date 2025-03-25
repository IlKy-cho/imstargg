package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionJpaRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrawlerPairBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerPairBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerPairBattleResultStatisticsCollectionJpaRepository jpaRepository;
    private final ConcurrentMap<Key, BrawlerPairBattleResultStatisticsCollectionEntity> cache = new ConcurrentHashMap<>();

    public BrawlerPairBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            BrawlerPairBattleResultStatisticsCollectionJpaRepository jpaRepository
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
        BattleResult battleResult = BattleResult.map(battle.getResult());
        new BattleCombinationBuilder(battle).pairWithTeam().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getStats(key).countUp(battleResult)
                )
        );
        return true;
    }

    private BrawlerPairBattleResultStatisticsCollectionEntity getStats(Key key) {
        return cache.computeIfAbsent(key, k -> Optional.ofNullable(k.trophyRange)
                .map(trophyRange -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        trophyRange,
                        k.battleDate(),
                        k.pairBrawlerBrawlStarsId()
                )).or(() -> Optional.ofNullable(k.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                                k.eventBrawlStarsId(),
                                k.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                k.battleDate(),
                                k.pairBrawlerBrawlStarsId()
                        )))
                .orElseGet(() -> new BrawlerPairBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        k.battleDate(),
                        k.pairBrawlerBrawlStarsId()
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
            long pairBrawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {

        static Key of(BrawlerPairBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    statsEntity.getPairBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null),
                    statsEntity.getBattleDate()
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
                    soloRankTierRange,
                    battle.getBattleTime().toLocalDate()
            ));
        }
    }

}
