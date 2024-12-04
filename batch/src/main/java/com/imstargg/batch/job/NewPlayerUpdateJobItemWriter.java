package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerUpdateJobItemWriter implements ItemWriter<NewPlayer>, InitializingBean {

    private final JpaItemWriter<Object> jpaItemWriter;

    public NewPlayerUpdateJobItemWriter(
            JpaItemWriter<Object> jpaItemWriter
    ) {
        this.jpaItemWriter = jpaItemWriter;
    }

    public NewPlayerUpdateJobItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends NewPlayer> items) throws Exception {
        List<Object> totalList = new ArrayList<>();

        for (NewPlayer item : items) {
            totalList.add(item.unknownPlayerEntity());
            if (item.playerEntity() != null) {
                totalList.add(item.playerEntity());
            }
        }

        jpaItemWriter.write(new Chunk<>(totalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
