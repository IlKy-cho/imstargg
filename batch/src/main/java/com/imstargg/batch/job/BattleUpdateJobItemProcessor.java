package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateApplier;
import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class BattleUpdateJobItemProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> {

    private static final Logger log = LoggerFactory.getLogger(BattleUpdateJobItemProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public BattleUpdateJobItemProcessor(
            Clock clock,
            BrawlStarsClient brawlStarsClient,
            BattleUpdateApplier battleUpdateApplier
    ) {
        this.clock = clock;
        this.brawlStarsClient = brawlStarsClient;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    @Override
    public PlayerBattleUpdateResult process(PlayerCollectionEntity item) throws Exception {
        if (!item.isNextUpdateCooldownOver(LocalDateTime.now(clock))) {
            log.warn("Player 업데이트 쿨타임이 지나지 않아 스킵. playerTag={}", item.getBrawlStarsTag());
            return null;
        }
        try {
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.getBrawlStarsTag());
            List<BattleCollectionEntity> updatedBattleEntities = battleUpdateApplier
                    .update(item, battleListResponse);
            List<LocalDateTime> updatedBattleTimes = updatedBattleEntities.stream()
                    .map(BattleCollectionEntity::getBattleTime)
                    .toList();
            
            item.battleUpdated(LocalDateTime.now(clock), updatedBattleTimes);

            return new PlayerBattleUpdateResult(item, updatedBattleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}",
                    item.getBrawlStarsTag());
            item.deleted();
            return new PlayerBattleUpdateResult(item, List.of());
        }
    }
}
