package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.collection.domain.PlayerBattleUpdateApplier;
import com.imstargg.collection.domain.PlayerUpdateApplier;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class PlayerUpdateProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> {

    private static final Logger log = LoggerFactory.getLogger(PlayerUpdateProcessor.class);

    private final BrawlStarsClient brawlStarsClient;
    private final PlayerUpdateApplier playerUpdateApplier;
    private final PlayerBattleUpdateApplier playerBattleUpdateApplier;

    public PlayerUpdateProcessor(
            BrawlStarsClient brawlStarsClient,
            PlayerUpdateApplier playerUpdateApplier,
            PlayerBattleUpdateApplier playerBattleUpdateApplier
    ) {
        this.brawlStarsClient = brawlStarsClient;
        this.playerUpdateApplier = playerUpdateApplier;
        this.playerBattleUpdateApplier = playerBattleUpdateApplier;
    }

    @Override
    public PlayerBattleUpdateResult process(PlayerCollectionEntity item) throws Exception {
        try {
            PlayerResponse playerResponse = brawlStarsClient.getPlayerInformation(item.getBrawlStarsTag());
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient.getPlayerRecentBattles(
                    item.getBrawlStarsTag());
            playerUpdateApplier.update(item, playerResponse);
            List<BattleCollectionEntity> battleEntities = playerBattleUpdateApplier.update(item, battleListResponse);
            return new PlayerBattleUpdateResult(item, battleEntities);
        } catch (BrawlStarsClientException.NotFound ex) {
            log.info("Player[{}] 가 존재하지 않는 것으로 확인되어 삭제.", item.getBrawlStarsTag());
            item.deleted();
            return new PlayerBattleUpdateResult(item, List.of());
        }
    }

}
