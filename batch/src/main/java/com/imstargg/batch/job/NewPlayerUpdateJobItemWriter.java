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

    private final JpaItemWriter<PlayerCollectionEntity> playerWriter;
    private final JpaItemWriter<UnknownPlayerCollectionEntity> unknownPlayerWriter;

    public NewPlayerUpdateJobItemWriter(
            JpaItemWriter<PlayerCollectionEntity> playerWriter,
            JpaItemWriter<UnknownPlayerCollectionEntity> unknownPlayerWriter
    ) {
        this.playerWriter = playerWriter;
        this.unknownPlayerWriter = unknownPlayerWriter;
    }

    public NewPlayerUpdateJobItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<PlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<UnknownPlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends NewPlayer> chunk) throws Exception {
        List<PlayerCollectionEntity> players = new ArrayList<>();
        List<UnknownPlayerCollectionEntity> unknownPlayers = new ArrayList<>();

        for (NewPlayer item : chunk) {
            players.add(item.playerEntity());
            unknownPlayers.add(item.unknownPlayerEntity());
        }

        playerWriter.write(new Chunk<>(players));
        unknownPlayerWriter.write(new Chunk<>(unknownPlayers));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        playerWriter.afterPropertiesSet();
    }
}
