package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.core.enums.PlayerStatus;
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
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;
    private final PlayerCollectionEntity playerEntity;
    private final List<BattleCollectionEntity> updatedBattleEntities = new ArrayList<>();
    private boolean playerUpdated = false;

    PlayerUpdater(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier,
            PlayerCollectionEntity playerEntity
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
        this.playerEntity = playerEntity;
    }

    public void update() {
        updatePlayer();
        updatePlayerBattle();

        playerEntity.playerUpdated(clock);
        if (playerEntity.getStatus() == PlayerStatus.DORMANT) {
            log.info("플레이어가 휴면상태 처리됨 playerTag={}", playerEntity.getBrawlStarsTag());
        }
    }

    public void updatePlayer() {
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(playerEntity.getBrawlStarsTag());
            playerEntity.update(
                    playerResponse.name(),
                    playerResponse.nameColor(),
                    playerResponse.icon().id(),
                    playerResponse.trophies(),
                    playerResponse.highestTrophies(),
                    playerResponse.expLevel(),
                    playerResponse.expPoints(),
                    playerResponse.isQualifiedFromChampionshipChallenge(),
                    playerResponse.victories3vs3(),
                    playerResponse.soloVictories(),
                    playerResponse.duoVictories(),
                    playerResponse.bestRoboRumbleTime(),
                    playerResponse.bestTimeAsBigBrawler(),
                    playerResponse.club().tag()
            );
            for (BrawlerStatResponse brawlerResponse : playerResponse.brawlers()) {
                playerEntity.updateBrawler(
                        brawlerResponse.id(),
                        brawlerResponse.power(),
                        brawlerResponse.rank(),
                        brawlerResponse.trophies(),
                        brawlerResponse.highestTrophies(),
                        brawlerResponse.gears().stream().map(GearStatResponse::id).toList(),
                        brawlerResponse.starPowers().stream().map(StarPowerResponse::id).toList(),
                        brawlerResponse.gadgets().stream().map(AccessoryResponse::id).toList()
                );
            }
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", playerEntity.getBrawlStarsTag());
            playerEntity.deleted();
        }
        playerUpdated = true;
    }

    public void updatePlayerBattle() {
        if (!playerUpdated) {
            throw new IllegalStateException("Player 정보를 먼저 업데이트 해야 합니다.");
        }
        if (playerEntity.getStatus() == PlayerStatus.DELETED) {
            log.debug("삭제된 플레이어는 배틀 업데이트를 스킵합니다. playerTag={}", playerEntity.getBrawlStarsTag());
            return;
        }
        ListResponse<BattleResponse> battleListResponse = this.brawlStarsClient.getPlayerRecentBattles(
                playerEntity.getBrawlStarsTag());
        updatedBattleEntities.addAll(battleUpdateApplier.update(playerEntity, battleListResponse));
    }

    public PlayerCollectionEntity getPlayerEntity() {
        return playerEntity;
    }

    public List<BattleCollectionEntity> getUpdatedBattleEntities() {
        return Collections.unmodifiableList(updatedBattleEntities);
    }
}
