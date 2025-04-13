package com.imstargg.batch.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class JdbcBatchItemInsertUpdateWriter<T> implements ItemWriter<T>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JdbcBatchItemInsertUpdateWriter.class);

    private final JdbcBatchItemWriter<T> insertItemWriter;

    private final JdbcBatchItemWriter<T> updateItemWriter;

    private final Predicate<T> insertCondition;

    public JdbcBatchItemInsertUpdateWriter(
            JdbcBatchItemWriter<T> insertItemWriter,
            JdbcBatchItemWriter<T> updateItemWriter,
            Predicate<T> insertCondition
    ) {
        this.insertItemWriter = insertItemWriter;
        this.updateItemWriter = updateItemWriter;
        this.insertCondition = insertCondition;
    }

    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        List<T> itemsToInsert = new ArrayList<>();
        List<T> itemsToUpdate = new ArrayList<>();

        for (T item : chunk) {
            if (insertCondition.test(item)) {
                itemsToInsert.add(item);
            } else {
                itemsToUpdate.add(item);
            }
        }

        log.debug("Total {} items to insert and {} items to update", itemsToInsert.size(), itemsToUpdate.size());

        if (!itemsToInsert.isEmpty()) {
            insertItemWriter.write(new Chunk<>(itemsToInsert));
        }
        if (!itemsToUpdate.isEmpty()) {
            updateItemWriter.write(new Chunk<>(itemsToUpdate));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        insertItemWriter.afterPropertiesSet();
        updateItemWriter.afterPropertiesSet();
    }
}
