package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerDeleter;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.UnknownPlayerStatus;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class NewPlayerUpdateJobItemProcessor
        implements ItemProcessor<UnknownPlayerCollectionEntity, PlayerUpdatedEntity> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerUpdateJobItemProcessor.class);

    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdater playerUpdater;
    private final PlayerDeleter playerDeleter;

    public NewPlayerUpdateJobItemProcessor(
            BrawlStarsClient brawlStarsClient,
            PlayerUpdater playerUpdater,
            PlayerDeleter playerDeleter
    ) {
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdater = playerUpdater;
        this.playerDeleter = playerDeleter;
    }

    @Override
    public PlayerUpdatedEntity process(UnknownPlayerCollectionEntity item) throws Exception {
        try {
            item.setStatus(UnknownPlayerStatus.UPDATED);
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.getBrawlStarsTag());
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.getBrawlStarsTag());

            return playerUpdater.create(playerResponse, battleListResponse);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getBrawlStarsTag());
            playerDeleter.delete(item);
            return null;
        }
    }

}
