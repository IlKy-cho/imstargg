package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerUpdater {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdater.class);

    private final Clock clock;
    private final PlayerUpdateProcessor playerUpdateProcessor;
    private final PlayerBattleUpdateProcessor playerBattleUpdateProcessor;
    private final PlayerCollectionEntity playerEntity;
    private final List<BattleCollectionEntity> updatedBattleEntities = new ArrayList<>();

    PlayerUpdater(
            Clock clock,
            PlayerUpdateProcessor playerUpdateProcessor,
            PlayerBattleUpdateProcessor playerBattleUpdateProcessor,
            PlayerCollectionEntity playerEntity
    ) {
        this.clock = clock;
        this.playerUpdateProcessor = playerUpdateProcessor;
        this.playerBattleUpdateProcessor = playerBattleUpdateProcessor;
        this.playerEntity = playerEntity;
    }

    public void update() {
        try {
            updatePlayer();
            updatePlayerBattle();
            playerEntity.playerUpdated(clock, updatedBattleEntities);
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", playerEntity.getBrawlStarsTag());
            playerEntity.deleted();
        }

    }

    public void updatePlayer() {
        playerUpdateProcessor.update(playerEntity);
    }

    private void updatePlayerBattle() {
        updatedBattleEntities.addAll(playerBattleUpdateProcessor.update(playerEntity));
    }

    public PlayerCollectionEntity getPlayerEntity() {
        return playerEntity;
    }

    public List<BattleCollectionEntity> getUpdatedBattleEntities() {
        return Collections.unmodifiableList(updatedBattleEntities);
    }
}
