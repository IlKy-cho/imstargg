package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.imstargg.storage.db.core.QBattleEntity.battleEntity;

class BattleJpaRepositoryCustomImpl implements BattleJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Long> findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
            @Nullable Collection<BattleType> battleTypes, @Nullable LocalDateTime battleTime
    ) {
        return queryFactory
                .select(
                        battleEntity.event.brawlStarsId,
                        battleEntity.type,
                        battleEntity.battleTime.max()
                )
                .from(battleEntity)
                .groupBy(battleEntity.event.brawlStarsId, battleEntity.type)
                .having(
                        battleEntity.event.brawlStarsId.isNotNull(),
                        battleEntity.event.brawlStarsId.gt(0),
                        battleTypeNotNull(battleTypes),
                        battleTypeIn(battleTypes),
                        battleTimeMaxGreaterThanEqual(battleTime)
                )
                .fetch()
                .stream()
                .map(tuple -> tuple.get(battleEntity.event.brawlStarsId))
                .distinct()
                .toList();
    }

    private BooleanExpression battleTypeNotNull(@Nullable Collection<BattleType> battleTypes) {
        return battleTypes != null ? battleEntity.type.isNotNull() : null;
    }

    private BooleanExpression battleTypeIn(@Nullable Collection<BattleType> battleTypes) {
        return battleTypes != null ? battleEntity.type.in(battleTypes.stream().map(BattleType::getCode).toList()) : null;
    }

    private BooleanExpression battleTimeMaxGreaterThanEqual(@Nullable LocalDateTime battleTime) {
        return battleTime != null ? battleEntity.battleTime.max().goe(battleTime) : null;
    }

    public Optional<BattleEntity> findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(
            long eventBrawlStarsId, @Nullable Collection<BattleType> battleTypes
    ) {
        List<String> battleTypeCodes = Optional.ofNullable(battleTypes)
                .map(types -> types.stream().map(BattleType::getCode).toList())
                .orElseGet(() -> selectDistinctBattleTypesByEventBrawlStarsId(eventBrawlStarsId));

        return battleTypeCodes
                .stream()
                .map(battleTypeCode ->
                        selectLatestBattleByEventBrawlStarsIdAndBattleType(eventBrawlStarsId, battleTypeCode)
                )
                .filter(Objects::nonNull)
                .max(Comparator.comparing(BattleEntity::getBattleTime));
    }

    private List<String> selectDistinctBattleTypesByEventBrawlStarsId(long eventBrawlStarsId) {
        return queryFactory
                .select(battleEntity.type).distinct()
                .from(battleEntity)
                .where(battleEntity.event.brawlStarsId.eq(eventBrawlStarsId))
                .fetch();
    }

    private BattleEntity selectLatestBattleByEventBrawlStarsIdAndBattleType(
            long eventBrawlStarsId, @Nullable String battleType
    ) {
        return queryFactory
                .selectFrom(battleEntity)
                .where(
                        battleEntity.event.brawlStarsId.eq(eventBrawlStarsId),
                        battleType != null ? battleEntity.type.eq(battleType) : battleEntity.type.isNull()
                )
                .orderBy(battleEntity.battleTime.desc())
                .fetchFirst();
    }
}
