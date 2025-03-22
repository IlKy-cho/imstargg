package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.collection.domain.PlayerUpdater;
import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

public class PlayerUpdateProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> {

    private final PlayerUpdaterFactory playerUpdaterFactory;

    public PlayerUpdateProcessor(
            PlayerUpdaterFactory playerUpdaterFactory
    ) {
        this.playerUpdaterFactory = playerUpdaterFactory;
    }

    @Override
    public PlayerBattleUpdateResult process(PlayerCollectionEntity item) throws Exception {
        PlayerUpdater playerUpdater = playerUpdaterFactory.create(item);
        playerUpdater.update();
        return new PlayerBattleUpdateResult(
                playerUpdater.getPlayerEntity(), playerUpdater.getUpdatedBattleEntities()
        );
    }

}
