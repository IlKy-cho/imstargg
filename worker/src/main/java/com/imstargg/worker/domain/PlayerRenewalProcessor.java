package com.imstargg.worker.domain;

import com.imstargg.collection.domain.PlayerUpdater;
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

    private final PlayerRepository playerRepository;
    private final PlayerUpdaterFactory playerUpdaterFactory;

    public PlayerRenewalProcessor(
            PlayerRepository playerRepository,
            PlayerUpdaterFactory playerUpdaterFactory
    ) {
        this.playerRepository = playerRepository;
        this.playerUpdaterFactory = playerUpdaterFactory;
    }

    public void renewPlayer(PlayerCollectionEntity playerEntity) {
        log.debug("기존 플레이어 갱신 tag={}", playerEntity.getBrawlStarsTag());
        PlayerUpdater playerUpdater = playerUpdaterFactory.create(playerEntity);
        playerUpdater.update();
        playerRepository.update(playerUpdater.getPlayerEntity(), playerUpdater.getUpdatedBattleEntities());

    }

    public void renewNewPlayer(UnknownPlayerCollectionEntity unknownPlayerEntity) {
        log.debug("신규 플레이어 갱신 tag={}", unknownPlayerEntity.getBrawlStarsTag());
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntity(unknownPlayerEntity.getBrawlStarsTag());
        PlayerUpdater playerUpdater = playerUpdaterFactory.create(playerEntity);
        playerUpdater.update();
        if (playerUpdater.getPlayerEntity().getStatus() == PlayerStatus.DELETED) {
            unknownPlayerEntity.notFound();
            playerRepository.update(unknownPlayerEntity);
        } else {
            playerRepository.update(
                    unknownPlayerEntity,
                    playerUpdater.getPlayerEntity(),
                    playerUpdater.getUpdatedBattleEntities()
            );
        }

    }
}
