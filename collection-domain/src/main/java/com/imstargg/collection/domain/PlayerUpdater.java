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
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayerBrawler;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayerUpdater {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdater.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleCollectionEntityFactory battleEntityFactory;
    private final PlayerCollectionEntity playerEntity;
    private final List<BattleCollectionEntity> updatedBattleEntities = new ArrayList<>();

    PlayerUpdater(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            BattleCollectionEntityFactory battleEntityFactory,
            PlayerCollectionEntity playerEntity
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.battleEntityFactory = battleEntityFactory;
        this.playerEntity = playerEntity;
    }

    public void update() {
        try {
            updatePlayer();
            updatePlayerBattle();
            updateLastBattleTime();
            updatePlayerStatus();
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", playerEntity.getBrawlStarsTag());
            playerEntity.deleted();
        }

    }

    public void updatePlayer() {
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
    }

    private void updatePlayerBattle() {
        getBattleResponseListToUpdate().stream()
                .map(battleResponse -> battleEntityFactory.create(playerEntity, battleResponse))
                .sorted(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .forEach(updatedBattleEntities::add);
        updateSoloRankTier();
    }

    private List<BattleResponse> getBattleResponseListToUpdate() {
        ListResponse<BattleResponse> battleListResponse = this.brawlStarsClient.getPlayerRecentBattles(
                playerEntity.getBrawlStarsTag());
        return Optional.ofNullable(playerEntity.getLatestBattleTime())
                .map(battle -> battleListResponse.items().stream()
                        .filter(battleResponse -> battleResponse.battleTime().isAfter(
                                playerEntity.getLatestBattleTime()))
                        .toList()
                ).orElse(battleListResponse.items());
    }

    private void updateSoloRankTier() {
        updatedBattleEntities
                .stream()
                .filter(battle -> Objects.equals(battle.getType(), BattleType.SOLO_RANKED.getCode()))
                .max(Comparator.comparing(BattleCollectionEntity::getBattleTime))
                .map(battle -> battle.findMe().getFirst())
                .ifPresent(latestSoloRankBattlePlayer ->
                        playerEntity.updateSoloRankTier(latestSoloRankBattlePlayer.getBrawler().getTrophies())
                );
    }

    private void updateLastBattleTime() {
        updatedBattleEntities
                .stream()
                .map(BattleCollectionEntity::getBattleTime)
                .max(Comparator.naturalOrder())
                .ifPresent(playerEntity::updateLatestBattleTime);
    }

    private void updatePlayerStatus() {
        if (containsInvalidBrawlerInfo()) {
            playerEntity.ai();
        } else {
            playerEntity.playerUpdated(clock);
        }
    }

    private boolean containsInvalidBrawlerInfo() {
        Map<Long, PlayerBrawlerCollectionEntity> brawlStarsIdToPlayerBrawler = playerEntity.getBrawlers().stream()
                .collect(Collectors.toMap(PlayerBrawlerCollectionEntity::getBrawlerBrawlStarsId, Function.identity()));

        for (BattleCollectionEntity battle : updatedBattleEntities) {
            List<BattleCollectionEntityTeamPlayer> myPlayers = battle.getTeams().stream()
                    .flatMap(Collection::stream)
                    .filter(player -> player.getBrawlStarsTag().equals(playerEntity.getBrawlStarsTag()))
                    .toList();

            for (BattleCollectionEntityTeamPlayer myPlayer : myPlayers) {
                BattleCollectionEntityTeamPlayerBrawler battlePlayerBrawler = myPlayer.getBrawler();
                if (!brawlStarsIdToPlayerBrawler.containsKey(battlePlayerBrawler.getBrawlStarsId())) {
                    return true;
                }
                PlayerBrawlerCollectionEntity playerBrawler = brawlStarsIdToPlayerBrawler.get(
                        battlePlayerBrawler.getBrawlStarsId());
                if (playerBrawler.getPower() < battlePlayerBrawler.getPower()) {
                    return true;
                }
                BattleType battleType = BattleType.find(battle.getType());
                if (battleType == BattleType.RANKED
                        && playerBrawler.getHighestTrophies() < battlePlayerBrawler.getTrophies()) {
                    return true;
                }
            }
        }

        return false;
    }

    public PlayerCollectionEntity getPlayerEntity() {
        return playerEntity;
    }

    public List<BattleCollectionEntity> getUpdatedBattleEntities() {
        return Collections.unmodifiableList(updatedBattleEntities);
    }
}
