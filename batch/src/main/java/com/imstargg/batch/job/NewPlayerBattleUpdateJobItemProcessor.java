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

public class NewPlayerBattleUpdateJobItemProcessor implements ItemProcessor<PlayerCollectionEntity, PlayerBattleUpdateResult> {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerBattleUpdateJobItemProcessor.class);

    private final Clock clock;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleUpdateApplier battleUpdateApplier;

    public NewPlayerBattleUpdateJobItemProcessor(
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
        try {
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.getBrawlStarsTag());
            if (battleListResponse.items().isEmpty()) {
                throw new IllegalStateException("BattleResponse 가 비어있습니다. playerTag=" + item.getBrawlStarsTag());
            }

            List<BattleCollectionEntity> updatedBattleEntities = battleUpdateApplier.update(item, battleListResponse);
            List<LocalDateTime> updatedBattleTimes = updatedBattleEntities.stream()
                    .map(BattleCollectionEntity::getBattleTime)
                    .toList();
            item.battleUpdated(LocalDateTime.now(clock), updatedBattleTimes);
            item.setStatus(PlayerStatus.BATTLE_UPDATED);

            return new PlayerBattleUpdateResult(item, updatedBattleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getBrawlStarsTag());
            item.delete();
            return new PlayerBattleUpdateResult(item, List.of());
        }
    }
}
