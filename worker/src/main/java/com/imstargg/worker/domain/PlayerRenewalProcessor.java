package com.imstargg.worker.domain;

import com.imstargg.collection.domain.PlayerUpdaterFactory;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlayerRenewalProcessor {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewalProcessor.class);

    private final PlayerUpdater playerUpdater;
    private final PlayerUpdaterFactory playerUpdaterFactory;

    public PlayerRenewalProcessor(
            PlayerUpdater playerUpdater,
            PlayerUpdaterFactory playerUpdaterFactory
    ) {
        this.playerUpdater = playerUpdater;
        this.playerUpdaterFactory = playerUpdaterFactory;
    }

    public void renewPlayer(PlayerCollectionEntity playerEntity) {
        log.debug("기존 플레이어 갱신 tag={}", playerEntity.getBrawlStarsTag());
        var updater = playerUpdaterFactory.create(playerEntity);
        updater.update();
        this.playerUpdater.update(updater.getPlayerEntity(), updater.getUpdatedBattleEntities());

    }

    public void renewNewPlayer(UnknownPlayerCollectionEntity unknownPlayerEntity) {
        log.debug("신규 플레이어 갱신 tag={}", unknownPlayerEntity.getBrawlStarsTag());
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntity(unknownPlayerEntity.getBrawlStarsTag());
        var updater = playerUpdaterFactory.create(playerEntity);
        updater.update();
        if (updater.getPlayerEntity().getStatus() == PlayerStatus.DELETED) {
            unknownPlayerEntity.notFound();
            this.playerUpdater.update(unknownPlayerEntity);
        } else {
            this.playerUpdater.update(
                    unknownPlayerEntity,
                    updater.getPlayerEntity(),
                    updater.getUpdatedBattleEntities()
            );
        }

    }
}
