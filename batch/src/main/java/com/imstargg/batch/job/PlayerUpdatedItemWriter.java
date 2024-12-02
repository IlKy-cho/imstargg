package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateResult;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdatedItemWriter implements ItemWriter<PlayerUpdatedEntity>, InitializingBean {

    private final JpaItemWriter<Object> jpaItemWriter;

    public PlayerUpdatedItemWriter(EntityManagerFactory emf) {
        this.jpaItemWriter = new JpaItemWriterBuilder<Object>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

    @Override
    public void write(Chunk<? extends PlayerUpdatedEntity> chunk) throws Exception {
        List<Object> totalList = new ArrayList<>();

        for (PlayerUpdatedEntity item : chunk) {
            totalList.add(item.playerEntity());
            totalList.addAll(item.playerBrawlerEntities());
            totalList.addAll(item.battleUpdateResults().stream()
                    .map(BattleUpdateResult::battleEntity).toList()
            );
            totalList.addAll(
                    item.battleUpdateResults().stream()
                            .flatMap(battleResult -> battleResult.battlePlayerEntities().stream())
                            .toList()
            );
        }

        jpaItemWriter.write(new Chunk<>(totalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
