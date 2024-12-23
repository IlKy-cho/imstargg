package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.collection.domain.BattleUpdateApplier;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRenewer {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewer.class);

    private final Clock clock;
    private final PlayerRepository playerRepository;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public PlayerRenewer(
            Clock clock,
            PlayerRepository playerRepository,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    public void renew(String tag) {
        Optional<PlayerCollectionEntity> playerEntityOpt = playerRepository.find(tag);
        if (playerEntityOpt.isPresent()) {
            renewPlayer(playerEntityOpt.get());
            return;
        }
        Optional<UnknownPlayerCollectionEntity> unknownPlayerEntityOpt = playerRepository.findUnknown(tag);
        if (unknownPlayerEntityOpt.isPresent()) {
            renewNewPlayer(unknownPlayerEntityOpt.get());
            return;
        }

        log.warn("플레이어가 존재하지 않습니다. tag={}", tag);
    }

    private void renewPlayer(PlayerCollectionEntity playerEntity) {
        if (playerEntity.isNextUpdateCooldownOver(clock)) {
            log.warn("플레이어 정보 갱신 대기 중입니다. tag={}", playerEntity.getBrawlStarsTag());
            return;
        }

        try {
            List<BattleCollectionEntity> updatedBattleEntities = updateBattles(playerEntity);

            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(playerEntity.getBrawlStarsTag());
            updatePlayer(playerEntity, playerResponse);

            playerRepository.update(playerEntity, updatedBattleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("플레이어가 존재하지 않아 삭제합니다. tag={}", playerEntity.getBrawlStarsTag());
            playerEntity.deleted();
            playerRepository.update(playerEntity);
        }
    }

    private void updatePlayer(PlayerCollectionEntity playerEntity, PlayerResponse playerResponse) {
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
    }

    public void renewNewPlayer(UnknownPlayerCollectionEntity unknownPlayerEntity) {
        unknownPlayerEntity.updated();
        List<BattleCollectionEntity> battleEntities = new ArrayList<>();
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(unknownPlayerEntity.getBrawlStarsTag());
            PlayerCollectionEntity playerEntity = newPlayer(playerResponse);

            updateBattles(playerEntity);

            playerRepository.update(unknownPlayerEntity, playerEntity, battleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("플레이어가 존재하지 않아 삭제합니다. tag={}", unknownPlayerEntity.getBrawlStarsTag());
            unknownPlayerEntity.notFound();
            playerRepository.update(unknownPlayerEntity);
        }
    }

    private List<BattleCollectionEntity> updateBattles(PlayerCollectionEntity playerEntity) {
        ListResponse<BattleResponse> battleListResponse = brawlStarsClient.getPlayerRecentBattles(playerEntity.getBrawlStarsTag());
        List<BattleCollectionEntity> updatedBattleEntities = battleUpdateApplier.update(playerEntity, battleListResponse);
        List<LocalDateTime> updatedBattleTimes = updatedBattleEntities.stream()
                .map(BattleCollectionEntity::getBattleTime)
                .toList();

        playerEntity.battleUpdated(updatedBattleTimes);
        return updatedBattleEntities;
    }

    private PlayerCollectionEntity newPlayer(PlayerResponse playerResponse) {
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntity(
                playerResponse.tag(),
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
                    brawlerResponse.trophies(),
                    brawlerResponse.highestTrophies(),
                    brawlerResponse.power(),
                    brawlerResponse.rank(),
                    brawlerResponse.gears().stream().map(GearStatResponse::id).toList(),
                    brawlerResponse.starPowers().stream().map(StarPowerResponse::id).toList(),
                    brawlerResponse.gadgets().stream().map(AccessoryResponse::id).toList()
            );
        }
        playerEntity.renewRequested();
        return playerRepository.add(playerEntity);
    }
}
