package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QBattleEventCollectionEntity.battleEventCollectionEntity;

public class BattleEventCollectionJpaRepositoryCustomImpl implements BattleEventCollectionJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleEventCollectionJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BattleCollectionEntity> findAllNotRegisteredEventBattle() {
        List<Long> eventBrawlStarsIds = queryFactory.select(battleCollectionEntity.event.eventBrawlStarsId)
                .distinct()
                .from(battleCollectionEntity)
                .where(
                        battleCollectionEntity.event.eventBrawlStarsId.isNotNull(),
                        battleCollectionEntity.event.eventBrawlStarsId.gt(0)
                )
                .fetch();
        Set<Long> registeredEventBrawlStarsIds = queryFactory.selectFrom(battleEventCollectionEntity)
                .fetch().stream().map(BattleEventCollectionEntity::getBrawlStarsId).collect(Collectors.toSet());

        return eventBrawlStarsIds.stream()
                .filter(id -> !registeredEventBrawlStarsIds.contains(id))
                .map(id ->
                        queryFactory
                                .selectFrom(battleCollectionEntity)
                                .where(battleCollectionEntity.event.eventBrawlStarsId.eq(id))
                                .fetchFirst()
                ).toList();
    }
}
