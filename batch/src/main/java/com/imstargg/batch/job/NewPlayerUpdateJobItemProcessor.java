package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayer;
import com.imstargg.batch.domain.PlayerDeleter;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;

public class NewPlayerUpdateJobItemProcessor
        implements ItemProcessor<UnknownPlayerCollectionEntity, NewPlayer> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerUpdateJobItemProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerDeleter playerDeleter;

    public NewPlayerUpdateJobItemProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            PlayerDeleter playerDeleter
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.playerDeleter = playerDeleter;
    }

    @Override
    public NewPlayer process(UnknownPlayerCollectionEntity item) throws Exception {
        try {
            item.setStatus(UnknownPlayerStatus.UPDATED);
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.getBrawlStarsTag());
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
                    playerResponse.club().tag(),
                    LocalDateTime.now(clock)
            );
            return new NewPlayer(item, playerEntity);
        }
        catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getBrawlStarsTag());
            playerDeleter.delete(item);
            return null;
        }
    }

}
