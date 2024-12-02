package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class PlayerUpdateJobItemProcessor implements ItemProcessor<PlayerToUpdateEntity, List<Object>> {

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdater playerUpdater;

    public PlayerUpdateJobItemProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            PlayerUpdater playerUpdater
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdater = playerUpdater;
    }

    @Override
    public List<Object> process(PlayerToUpdateEntity item) throws Exception {
        if (!item.playerEntity().isNextUpdateCooldownOver(LocalDateTime.now(clock))) {
            item.playerEntity().setStatus(PlayerStatus.UPDATED);
            return List.of(item.playerEntity());
        }

        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.playerEntity().getBrawlStarsTag());
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.playerEntity().getBrawlStarsTag());

            PlayerUpdatedEntity updatedEntity = playerUpdater.update(item, playerResponse, battleListResponse);

            return updatedEntity.toList();
        } catch (BrawlStarsClientNotFoundException ex) {
            item.playerEntity().setStatus(PlayerStatus.DELETED);
            return List.of(item.playerEntity());
        }
    }

}
