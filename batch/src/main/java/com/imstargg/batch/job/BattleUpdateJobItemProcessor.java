package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateApplier;
import com.imstargg.batch.domain.PlayerBattleUpdateResult;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerLastBattleProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class BattleUpdateJobItemProcessor implements ItemProcessor<PlayerLastBattleProjection, PlayerBattleUpdateResult> {

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
    public PlayerBattleUpdateResult process(PlayerLastBattleProjection item) throws Exception {
        try {
            ListResponse<BattleResponse> battleListResponse = brawlStarsClient
                    .getPlayerRecentBattles(item.getPlayer().getBrawlStarsTag());
            List<BattleCollectionEntity> updatedBattleEntities = battleUpdateApplier
                    .update(item.getPlayer(), battleListResponse, item.getLastBattle());
            List<LocalDateTime> updatedBattleTimes = updatedBattleEntities.stream()
                    .map(BattleCollectionEntity::getBattleTime)
                    .toList();
            item.getPlayer().nextUpdateWeight(LocalDateTime.now(clock), updatedBattleTimes);
            item.getPlayer().setStatus(PlayerStatus.BATTLE_UPDATED);

            return new PlayerBattleUpdateResult(item.getPlayer(), updatedBattleEntities);
        } catch (BrawlStarsClientNotFoundException ex) {
            log.warn("Player 가 존재하지 않는 것으로 확인되어 삭제. playerTag={}", item.getPlayer().getBrawlStarsTag());
            item.getPlayer().delete();
            return new PlayerBattleUpdateResult(item.getPlayer(), List.of());
        }
    }
}
