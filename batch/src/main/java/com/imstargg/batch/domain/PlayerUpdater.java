package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PlayerUpdater {

    private final Clock clock;
    private final PlayerUpdateApplier playerUpdateApplier;
    private final PlayerBrawlerUpdateApplier playerBrawlerUpdateApplier;
    private final BattleUpdateApplier battleUpdateApplier;

    public PlayerUpdater(
            Clock clock,
            PlayerUpdateApplier playerUpdateApplier,
            PlayerBrawlerUpdateApplier playerBrawlerUpdateApplier,
            BattleUpdateApplier battleUpdateApplier) {
        this.clock = clock;
        this.playerUpdateApplier = playerUpdateApplier;
        this.playerBrawlerUpdateApplier = playerBrawlerUpdateApplier;
        this.battleUpdateApplier = battleUpdateApplier;
    }

    public PlayerUpdatedEntity update(
            PlayerToUpdateEntity playerToUpdateEntity,
            PlayerResponse playerResponse,
            ListResponse<BattleResponse> battleListResponse
    ) {
        PlayerCollectionEntity updatedPlayer = playerUpdateApplier.update(playerToUpdateEntity.playerEntity(), playerResponse);
        List<PlayerBrawlerCollectionEntity> updatedBrawlers = playerBrawlerUpdateApplier.update(
                updatedPlayer, playerToUpdateEntity.playerBrawlerEntities(), playerResponse.brawlers());
        List<BattleUpdateResult> updatedBattleResults = battleUpdateApplier.update(
                updatedPlayer, battleListResponse, playerToUpdateEntity.lastBattleEntity().orElse(null));
        List<LocalDateTime> updatedBattleTimes = updatedBattleResults.stream()
                .map(battle -> battle.battleEntity().getBattleTime())
                .toList();

        updatedPlayer.setStatus(PlayerStatus.UPDATED);
        updatedPlayer.nextUpdateWeight(LocalDateTime.now(clock), updatedBattleTimes);

        return new PlayerUpdatedEntity(
                updatedPlayer,
                updatedBrawlers,
                updatedBattleResults
        );
    }

    public PlayerUpdatedEntity create(
            PlayerResponse playerResponse,
            ListResponse<BattleResponse> battleListResponse
    ) {
        PlayerCollectionEntity updatedPlayer = playerUpdateApplier.create(playerResponse);
        List<PlayerBrawlerCollectionEntity> updatedBrawlers = playerBrawlerUpdateApplier.update(
                updatedPlayer, List.of(), playerResponse.brawlers());
        List<BattleUpdateResult> updatedBattleResults = battleUpdateApplier.update(
                updatedPlayer, battleListResponse, null);
        List<LocalDateTime> updatedBattleTimes = updatedBattleResults.stream()
                .map(battle -> battle.battleEntity().getBattleTime())
                .toList();

        updatedPlayer.setStatus(PlayerStatus.UPDATED);
        updatedPlayer.nextUpdateWeight(LocalDateTime.now(clock), updatedBattleTimes);

        return new PlayerUpdatedEntity(
                updatedPlayer,
                updatedBrawlers,
                updatedBattleResults
        );
    }
}
