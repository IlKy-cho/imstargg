package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.collection.domain.BattleUpdateApplier;
import com.imstargg.core.enums.PlayerRenewalStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerRenewalCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRenewer {

    private static final Logger log = LoggerFactory.getLogger(PlayerRenewer.class);

    private final Clock clock;
    private final PlayerRepository playerRepository;
    private final PlayerRenewalRepository playerRenewalRepository;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public PlayerRenewer(
            Clock clock,
            PlayerRepository playerRepository,
            PlayerRenewalRepository playerRenewalRepository,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.playerRepository = playerRepository;
        this.playerRenewalRepository = playerRenewalRepository;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    public void renew(String tag) {
        PlayerRenewalCollectionEntity playerRenewalEntity = playerRenewalRepository.find(tag)
                .orElseThrow(() -> new IllegalStateException("플레이어 갱신 정보가 존재하지 않습니다. tag=" + tag));
        if (PlayerRenewalStatus.PENDING != playerRenewalEntity.getStatus()) {
            log.warn("플레이어 갱신 상태가 PENDING이 아니므로 갱신하지 않습니다. tag={}", tag);
            return;
        }

        try {
            playerRenewalRepository.executing(playerRenewalEntity);

            Optional<PlayerCollectionEntity> playerEntityOpt = playerRepository.find(tag);
            if (playerEntityOpt.isPresent()) {
                renewPlayer(playerEntityOpt.get());
                log.info("플레이어 갱신 완료. tag={}", tag);
                return;
            }
            Optional<UnknownPlayerCollectionEntity> unknownPlayerEntityOpt = playerRepository.findUnknown(tag);
            if (unknownPlayerEntityOpt.isPresent()) {
                renewNewPlayer(unknownPlayerEntityOpt.get());
                log.info("신규 플레이어 갱신 완료. tag={}", tag);
                return;
            }

            log.warn("플레이어가 존재하지 않습니다. tag={}", tag);
        } catch(Exception e) {
            playerRenewalRepository.failed(playerRenewalEntity);
            throw e;
        } finally {
            playerRenewalRepository.complete(playerRenewalEntity);
        }
    }

    private void renewPlayer(PlayerCollectionEntity playerEntity) {
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(playerEntity.getBrawlStarsTag());
            updatePlayer(playerEntity, playerResponse);

            List<BattleCollectionEntity> updatedBattleEntities = updateBattles(playerEntity);

            playerEntity.playerUpdated(clock);
            playerRepository.update(playerEntity, updatedBattleEntities);
        } catch (BrawlStarsClientException.NotFound ex) {
            log.warn("플레이어가 존재하지 않아 삭제합니다. tag={}", playerEntity.getBrawlStarsTag());
            playerEntity.deleted();
            playerRepository.save(playerEntity);
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
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(unknownPlayerEntity.getBrawlStarsTag());
            PlayerCollectionEntity playerEntity = newPlayer(playerResponse);

            List<BattleCollectionEntity> updatedBattleEntities = updateBattles(playerEntity);

            playerEntity.playerUpdated(clock);
            playerRepository.update(unknownPlayerEntity, playerEntity, updatedBattleEntities);
        } catch (BrawlStarsClientException.NotFound ex) {
            log.warn("플레이어가 존재하지 않아 삭제합니다. tag={}", unknownPlayerEntity.getBrawlStarsTag());
            unknownPlayerEntity.notFound();
            playerRepository.update(unknownPlayerEntity);
        }
    }

    private List<BattleCollectionEntity> updateBattles(PlayerCollectionEntity playerEntity) {
        ListResponse<BattleResponse> battleListResponse = brawlStarsClient.getPlayerRecentBattles(
                playerEntity.getBrawlStarsTag());
        return battleUpdateApplier.update(playerEntity, battleListResponse);
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
        return playerRepository.save(playerEntity);
    }
}
