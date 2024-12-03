package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class PlayerBattleUpdateItemWriter implements ItemWriter<PlayerBattleUpdateResult>, InitializingBean {

    private final JpaItemWriter<Object> jpaItemWriter;

    public PlayerBattleUpdateItemWriter(
            JpaItemWriter<Object> jpaItemWriter
    ) {
        this.jpaItemWriter = jpaItemWriter;
    }

    public PlayerBattleUpdateItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends PlayerBattleUpdateResult> items) throws Exception {
        List<Object> totalList = new ArrayList<>();

        for (PlayerBattleUpdateResult item : items) {
            totalList.add(item.playerEntity());
            totalList.addAll(item.battleEntities());
        }

        jpaItemWriter.write(new Chunk<>(totalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
