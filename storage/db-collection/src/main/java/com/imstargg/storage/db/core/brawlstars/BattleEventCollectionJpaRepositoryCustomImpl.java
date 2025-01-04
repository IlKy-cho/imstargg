package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QBattleEventCollectionEntity.battleEventCollectionEntity;

class BattleEventCollectionJpaRepositoryCustomImpl implements BattleEventCollectionJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleEventCollectionJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BattleCollectionEntity> findAllNotRegisteredEventBattle() {
        List<Long> eventBrawlStarsIds = queryFactory.select(battleCollectionEntity.event.brawlStarsId)
                .distinct()
                .from(battleCollectionEntity)
                .where(
                        battleCollectionEntity.event.brawlStarsId.isNotNull(),
                        battleCollectionEntity.event.brawlStarsId.gt(0)
                )
                .fetch();
        Set<Long> registeredEventBrawlStarsIds = queryFactory.selectFrom(battleEventCollectionEntity)
                .fetch().stream().map(BattleEventCollectionEntity::getBrawlStarsId).collect(Collectors.toSet());

        return eventBrawlStarsIds.stream()
                .filter(id -> !registeredEventBrawlStarsIds.contains(id))
                .map(id -> findLatestBattle(id).orElseThrow())
                .toList();
    }

    @Override
    public Optional<BattleCollectionEntity> findLatestBattle(BattleEventCollectionEntity battleEvent) {
        return findLatestBattle(battleEvent.getBrawlStarsId());
    }

    private Optional<BattleCollectionEntity> findLatestBattle(long eventBrawlStarsId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(battleCollectionEntity)
                        .where(battleCollectionEntity.event.brawlStarsId.eq(eventBrawlStarsId))
                        .orderBy(battleCollectionEntity.battleTime.desc())
                        .fetchFirst()
        );
    }
}
