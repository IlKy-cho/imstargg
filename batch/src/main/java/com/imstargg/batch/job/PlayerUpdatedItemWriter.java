package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateResult;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdatedItemWriter implements ItemWriter<PlayerUpdatedEntity>, InitializingBean {

    private final JpaItemWriter<PlayerCollectionEntity> playerWriter;
    private final JpaItemWriter<PlayerBrawlerCollectionEntity> playerBrawlerWriter;
    private final JpaItemWriter<BattleCollectionEntity> battleWriter;
    private final JpaItemWriter<BattlePlayerCollectionEntity> battlePlayerWriter;

    public PlayerUpdatedItemWriter(
            JpaItemWriter<PlayerCollectionEntity> playerWriter,
            JpaItemWriter<PlayerBrawlerCollectionEntity> playerBrawlerWriter,
            JpaItemWriter<BattleCollectionEntity> battleWriter,
            JpaItemWriter<BattlePlayerCollectionEntity> battlePlayerWriter
    ) {
        this.playerWriter = playerWriter;
        this.playerBrawlerWriter = playerBrawlerWriter;
        this.battleWriter = battleWriter;
        this.battlePlayerWriter = battlePlayerWriter;
    }

    public PlayerUpdatedItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<PlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<PlayerBrawlerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<BattleCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<BattlePlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends PlayerUpdatedEntity> chunk) throws Exception {
        List<PlayerCollectionEntity> players = new ArrayList<>();
        List<PlayerBrawlerCollectionEntity> playerBrawlers = new ArrayList<>();
        List<BattleCollectionEntity> battles = new ArrayList<>();
        List<BattlePlayerCollectionEntity> battlePlayers = new ArrayList<>();

        for (PlayerUpdatedEntity item : chunk) {
            players.add(item.playerEntity());
            playerBrawlers.addAll(item.playerBrawlerEntities());
            battles.addAll(item.battleUpdateResults().stream()
                    .map(BattleUpdateResult::battleEntity).toList()
            );
            battlePlayers.addAll(
                    item.battleUpdateResults().stream()
                            .flatMap(battleResult -> battleResult.battlePlayerEntities().stream())
                            .toList()
            );
        }

        playerWriter.write(new Chunk<>(players));
        playerBrawlerWriter.write(new Chunk<>(playerBrawlers));
        battleWriter.write(new Chunk<>(battles));
        battlePlayerWriter.write(new Chunk<>(battlePlayers));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        playerWriter.afterPropertiesSet();
    }
}
