package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerUpdateProcessor {

    private final BrawlStarsClient brawlStarsClient;

    public PlayerUpdateProcessor(BrawlStarsClient brawlStarsClient) {
        this.brawlStarsClient = brawlStarsClient;
    }

    public void update(PlayerCollectionEntity playerEntity) {
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


}
