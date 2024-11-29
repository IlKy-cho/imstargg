package com.imstargg.batch.job.support;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {

    private final JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(Chunk<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        for (List<T> list : items) {
            totalList.addAll(list);
        }

        jpaItemWriter.write(new Chunk<>(totalList));
    }

    @Override
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    }

    @Override
    public void setClearPersistenceContext(boolean clearPersistenceContext) {
        jpaItemWriter.setClearPersistenceContext(clearPersistenceContext);
    }

    @Override
    public void setUsePersist(boolean usePersist) {
        jpaItemWriter.setUsePersist(usePersist);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
