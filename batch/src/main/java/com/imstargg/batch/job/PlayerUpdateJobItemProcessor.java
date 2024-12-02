package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerDeleter;
import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;

public class PlayerUpdateJobItemProcessor implements ItemProcessor<PlayerToUpdateEntity, PlayerUpdatedEntity> {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateJobItemProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdater playerUpdater;
    private final PlayerDeleter playerDeleter;

    public PlayerUpdateJobItemProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            PlayerUpdater playerUpdater,
            PlayerDeleter playerDeleter
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdater = playerUpdater;
        this.playerDeleter = playerDeleter;
    }

    @Override
    public PlayerUpdatedEntity process(PlayerToUpdateEntity item) throws Exception {
        if (!item.playerEntity().isNextUpdateCooldownOver(LocalDateTime.now(clock))) {
            return null;
        }

        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.playerEntity().getBrawlStarsTag());
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.playerEntity().getBrawlStarsTag());

            return playerUpdater.update(item, playerResponse, battleListResponse);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.playerEntity().getBrawlStarsTag());
            playerDeleter.delete(item.playerEntity());
            return null;
        }
    }

}
