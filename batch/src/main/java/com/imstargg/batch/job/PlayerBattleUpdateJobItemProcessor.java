package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateApplier;
import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class PlayerBattleUpdateJobItemProcessor implements ItemProcessor<BattleCollectionEntity, PlayerBattleUpdateResult> {

    private static final Logger log = LoggerFactory.getLogger(PlayerBattleUpdateJobItemProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public PlayerBattleUpdateJobItemProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    @Override
    public PlayerBattleUpdateResult process(BattleCollectionEntity item) throws Exception {
        PlayerCollectionEntity playerEntity = item.getPlayer().getPlayer();
        try {
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(playerEntity.getBrawlStarsTag());
            List<BattleCollectionEntity> updatedBattleEntities = battleUpdateApplier
                    .update(playerEntity, battleListResponse, item);
            List<LocalDateTime> updatedBattleTimes = updatedBattleEntities.stream()
                    .map(BattleCollectionEntity::getBattleTime)
                    .toList();
            
            playerEntity.nextUpdateWeight(LocalDateTime.now(clock), updatedBattleTimes);
            playerEntity.setStatus(PlayerStatus.BATTLE_UPDATED);

            return new PlayerBattleUpdateResult(playerEntity, updatedBattleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}",
                    playerEntity.getBrawlStarsTag());
            playerEntity.delete();
            return new PlayerBattleUpdateResult(playerEntity, List.of());
        }
    }
}
