package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFilter;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class NewPlayerJobItemProcessor
        implements ItemProcessor<BattleCollectionEntity, List<PlayerCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerJobItemProcessor.class);

    private final PlayerTagFilter playerTagFilter;
    private final BrawlStarsClient brawlStarsClient;

    public NewPlayerJobItemProcessor(PlayerTagFilter playerTagFilter, BrawlStarsClient brawlStarsClient) {
        this.playerTagFilter = playerTagFilter;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Override
    public List<PlayerCollectionEntity> process(BattleCollectionEntity item) throws Exception {
        List<String> newPlayerTags = fineNewPlayerTags(item);
        List<PlayerCollectionEntity> newPlayers = new ArrayList<>();

        for (String newPlayerTag : newPlayerTags) {
            try {
                PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(newPlayerTag);
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
                            brawlerResponse.power(),
                            brawlerResponse.rank(),
                            brawlerResponse.trophies(),
                            brawlerResponse.highestTrophies(),
                            brawlerResponse.gears().stream().map(GearStatResponse::id).toList(),
                            brawlerResponse.starPowers().stream().map(StarPowerResponse::id).toList(),
                            brawlerResponse.gadgets().stream().map(AccessoryResponse::id).toList()
                    );
                }
                newPlayers.add(playerEntity);
            } catch (BrawlStarsClientNotFoundException e) {
                log.info("새로운 Player 가 존재하지 않는 것으로 확인되어 스킵. playerTag={}", newPlayerTag);
            }
        }

        return newPlayers;
    }

    private List<String> fineNewPlayerTags(BattleCollectionEntity item) {
        return playerTagFilter.filter(
                item.getAllPlayers()
                        .stream()
                        .map(BattleCollectionEntityTeamPlayer::getBrawlStarsTag)
                        .toList()
        );
    }
}
