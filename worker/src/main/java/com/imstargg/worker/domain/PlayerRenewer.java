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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
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
        Optional<PlayerCollectionEntity> playerOpt = playerRepository.find(tag);
        if (playerOpt.isEmpty()) {
            log.warn("존재하지 않는 플레이어에 대해 갱신을 시도했습니다. tag={}", tag);
            return;
        }
        PlayerCollectionEntity playerEntity = playerOpt.get();
        List<BattleCollectionEntity> battleEntities = new ArrayList<>();

        try {
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient.getPlayerRecentBattles(tag);
            battleEntities.addAll(battleUpdateApplier.update(playerEntity, battleListResponse));
            List<LocalDateTime> updatedBattleTimes = battleEntities.stream()
                    .map(BattleCollectionEntity::getBattleTime)
                    .toList();

            playerEntity.battleUpdated(LocalDateTime.now(clock), updatedBattleTimes);

            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(tag);
            updatePlayer(playerEntity, playerResponse);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("플레이어가 존재하지 않아 삭제합니다. tag={}", tag);
            playerEntity.deleted();
        }

        try {
            playerRepository.update(playerEntity, battleEntities);
        } catch (OptimisticLockingFailureException ex) {
            log.warn("플레이어 정보 갱신에 실패했습니다. tag={}", tag, ex);
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
}
