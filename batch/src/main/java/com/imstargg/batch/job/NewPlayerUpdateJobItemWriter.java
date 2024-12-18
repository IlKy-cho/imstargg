package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerUpdateJobItemWriter implements ItemWriter<NewPlayer>, InitializingBean {

    private final JpaItemWriter<UnknownPlayerCollectionEntity> unknownPlayerJpaItemWriter;
    private final JpaItemWriter<PlayerCollectionEntity> playerJpaItemWriter;

    public NewPlayerUpdateJobItemWriter(
            JpaItemWriter<UnknownPlayerCollectionEntity> unknownPlayerJpaItemWriter,
            JpaItemWriter<PlayerCollectionEntity> playerJpaItemWriter
    ) {
        this.unknownPlayerJpaItemWriter = unknownPlayerJpaItemWriter;
        this.playerJpaItemWriter = playerJpaItemWriter;
    }

    public NewPlayerUpdateJobItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<UnknownPlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<PlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends NewPlayer> items) throws Exception {
        List<UnknownPlayerCollectionEntity> unknownPlayerTotalList = new ArrayList<>();
        List<PlayerCollectionEntity> playerTotalList = new ArrayList<>();

        for (NewPlayer item : items) {
            unknownPlayerTotalList.add(item.unknownPlayerEntity());
            if (item.playerEntity() != null) {
                playerTotalList.add(item.playerEntity());
            }
        }

        unknownPlayerJpaItemWriter.write(new Chunk<>(unknownPlayerTotalList));
        playerJpaItemWriter.write(new Chunk<>(playerTotalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        unknownPlayerJpaItemWriter.afterPropertiesSet();
    }
}
