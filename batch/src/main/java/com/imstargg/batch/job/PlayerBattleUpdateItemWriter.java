package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

public class PlayerBattleUpdateItemWriter implements ItemWriter<PlayerBattleUpdateResult>, InitializingBean {

    private final JpaItemWriter<PlayerCollectionEntity> playerJpaItemWriter;
    private final JpaItemWriter<BattleCollectionEntity> battleJpaItemWriter;

    public PlayerBattleUpdateItemWriter(
            JpaItemWriter<PlayerCollectionEntity> playerJpaItemWriter,
            JpaItemWriter<BattleCollectionEntity> battleJpaItemWriter
    ) {
        this.playerJpaItemWriter = playerJpaItemWriter;
        this.battleJpaItemWriter = battleJpaItemWriter;
    }

    public PlayerBattleUpdateItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<PlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<BattleCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends PlayerBattleUpdateResult> items) throws Exception {
        List<PlayerCollectionEntity> playerTotalList = new ArrayList<>();
        List<BattleCollectionEntity> battleTotalList = new ArrayList<>();

        for (PlayerBattleUpdateResult item : items) {
            playerTotalList.add(item.playerEntity());
            battleTotalList.addAll(item.battleEntities());
        }

        playerJpaItemWriter.write(new Chunk<>(playerTotalList));
        battleJpaItemWriter.write(new Chunk<>(battleTotalList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        playerJpaItemWriter.afterPropertiesSet();
    }
}
