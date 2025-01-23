package com.imstargg.batch.job;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;

public class PlayerUpdateProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;

    public PlayerUpdateProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Override
    public PlayerCollectionEntity process(PlayerCollectionEntity item) throws Exception {
        if (!item.isNextUpdateCooldownOver(clock)) {
            log.info("플레이어 업데이트 쿨타임이 아직 지나지 않아 스킵. playerTag={}", item.getBrawlStarsTag());
            return null;
        }
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
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getBrawlStarsTag());
            item.deleted();
            return item;
        }
    }

}
