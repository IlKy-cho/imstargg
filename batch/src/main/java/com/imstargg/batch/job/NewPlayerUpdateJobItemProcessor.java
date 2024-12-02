package com.imstargg.batch.job;

import com.imstargg.batch.domain.NewPlayerAppender;
import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class NewPlayerUpdateJobItemProcessor implements ItemProcessor<UnknownPlayerCollectionEntity, List<Object>> {

    private final BrawlStarsClient brawlStarsClient;
    private final NewPlayerAppender newPlayerAppender;
    private final PlayerUpdater playerUpdater;

    public NewPlayerUpdateJobItemProcessor(
            BrawlStarsClient brawlStarsClient,
            NewPlayerAppender newPlayerAppender,
            PlayerUpdater playerUpdater
    ) {
        this.brawlStarsClient = brawlStarsClient;
        this.newPlayerAppender = newPlayerAppender;
        this.playerUpdater = playerUpdater;
    }

    @Override
    public List<Object> process(UnknownPlayerCollectionEntity item) throws Exception {
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.getBrawlStarsTag());
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.getBrawlStarsTag());

            PlayerCollectionEntity playerEntity = newPlayerAppender.append(playerResponse);

            PlayerUpdatedEntity updatedEntity = playerUpdater.update(
                    new PlayerToUpdateEntity(playerEntity, List.of())
                    , playerResponse, battleListResponse);

            return updatedEntity.toList();
        } catch (BrawlStarsClientNotFoundException ex) {
            item.delete();
            return List.of(item);
        }
    }

}
