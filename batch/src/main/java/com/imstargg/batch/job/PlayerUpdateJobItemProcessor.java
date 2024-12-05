package com.imstargg.batch.job;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PlayerUpdateJobItemProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateJobItemProcessor.class);

    private final BrawlStarsClient brawlStarsClient;

    public PlayerUpdateJobItemProcessor(BrawlStarsClient brawlStarsClient) {
        this.brawlStarsClient = brawlStarsClient;
    }

    @Override
    public PlayerCollectionEntity process(PlayerCollectionEntity item) throws Exception {

        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.getBrawlStarsTag());
            item.update(
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
                item.updateBrawler(
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
            return item;
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getBrawlStarsTag());
            item.deleted();
            return item;
        }
    }

}
