package com.imstargg.batch.job;

import com.imstargg.batch.job.support.querydsl.QuerydslEntityCursorItemReader;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.imstargg.storage.db.core.player.QBattleCollectionEntity.battleCollectionEntity;

@Component
public class BattleItemReaderFactory {

    private final EntityManagerFactory entityManagerFactory;

    public BattleItemReaderFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public QuerydslEntityCursorItemReader<BattleCollectionEntity> create(
            int chunkSize, @Nullable Long fromId, @Nullable Long toId
    ) {
        var reader = new QuerydslEntityCursorItemReader<BattleCollectionEntity>(
                entityManagerFactory,
                chunkSize,
                (entity, queryFactory) -> queryFactory
                        .selectFrom(battleCollectionEntity)
                        .join(battleCollectionEntity.player.player).fetchJoin()
                        .where(
                                Optional.ofNullable(entity)
                                        .map(BattleCollectionEntity::getId)
                                        .or(() -> Optional.ofNullable(fromId))
                                        .map(battleCollectionEntity.id::goe)
                                        .orElse(null),
                                Optional.ofNullable(toId)
                                        .map(battleCollectionEntity.id::loe)
                                        .orElse(null)
                        )
                        .orderBy(battleCollectionEntity.id.asc())
        );
        reader.setTransacted(false);
        return reader;
    }
}
