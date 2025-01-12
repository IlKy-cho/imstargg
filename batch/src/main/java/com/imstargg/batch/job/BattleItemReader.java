package com.imstargg.batch.job;

import com.imstargg.batch.job.support.querydsl.Expression;
import com.imstargg.batch.job.support.querydsl.QuerydslNoOffsetIdPagingItemReader;
import com.imstargg.batch.job.support.querydsl.QuerydslNoOffsetNumberOptions;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDate;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

public class BattleItemReader implements ItemReader<BattleCollectionEntity>, InitializingBean {

    private final QuerydslNoOffsetIdPagingItemReader<BattleCollectionEntity, Long> reader;
    @Nullable private final LocalDate date;

    public BattleItemReader(
            EntityManagerFactory entityManagerFactory,
            int pageSize,
            @Nullable LocalDate date
    ) {
        var options = new QuerydslNoOffsetNumberOptions<BattleCollectionEntity, Long>(
                battleCollectionEntity.id,
                Expression.DESC
        );
        reader = new QuerydslNoOffsetIdPagingItemReader<>(
                entityManagerFactory,
                pageSize,
                options,
                queryFactory -> queryFactory
                        .selectFrom(battleCollectionEntity)
        );
        reader.setTransacted(false);
        this.date = date;
    }

    @Override
    public BattleCollectionEntity read() throws Exception {
        BattleCollectionEntity item = reader.read();
        if (item == null) {
            return null;
        }
        if (date != null && date.isAfter(item.getCreatedAt().toLocalDate())) {
            return null;
        }
        return item;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        reader.afterPropertiesSet();
    }
}
