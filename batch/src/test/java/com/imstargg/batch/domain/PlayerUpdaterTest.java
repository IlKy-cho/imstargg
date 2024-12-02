package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class PlayerUpdaterTest {

    private PlayerUpdater playerUpdater;

    private Clock clock;

    private PlayerUpdateApplier playerUpdateApplier;

    private PlayerBrawlerUpdateApplier playerBrawlerUpdateApplier;

    private BattleUpdateApplier battleUpdateApplier;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.systemDefault());
        playerUpdateApplier = mock(PlayerUpdateApplier.class);
        playerBrawlerUpdateApplier = mock(PlayerBrawlerUpdateApplier.class);
        battleUpdateApplier = mock(BattleUpdateApplier.class);

        playerUpdater = new PlayerUpdater(
                clock,
                playerUpdateApplier,
                playerBrawlerUpdateApplier,
                battleUpdateApplier
        );
    }

    @Test
    void 플레이어를_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        PlayerBrawlerCollectionEntity playerBrawler1 = mock(PlayerBrawlerCollectionEntity.class);
        PlayerBrawlerCollectionEntity playerBrawler2 = mock(PlayerBrawlerCollectionEntity.class);
        List<PlayerBrawlerCollectionEntity> playerBrawlerList = List.of(playerBrawler1, playerBrawler2);

        PlayerToUpdateEntity playerToUpdateEntity = new PlayerToUpdateEntity(
                playerEntity,
                playerBrawlerList,
                Optional.empty()
        );

        BrawlerStatResponse playerBrawlerResponse1 = mock(BrawlerStatResponse.class);
        BrawlerStatResponse playerBrawlerResponse2 = mock(BrawlerStatResponse.class);
        List<BrawlerStatResponse> playerBrawlerResponseList = List.of(playerBrawlerResponse1, playerBrawlerResponse2);
        PlayerResponse playerResponse = mock(PlayerResponse.class);
        given(playerResponse.brawlers()).willReturn(playerBrawlerResponseList);
        BattleResponse battleResponse = mock(BattleResponse.class);
        ListResponse<BattleResponse> battleResponseList = new ListResponse<>(List.of(battleResponse), null);

        PlayerCollectionEntity updatedPlayer = mock(PlayerCollectionEntity.class);
        given(updatedPlayer.getId()).willReturn(1L);
        given(playerUpdateApplier.update(playerEntity, playerResponse)).willReturn(updatedPlayer);

        PlayerBrawlerCollectionEntity updatedPlayerBrawler1 = mock(PlayerBrawlerCollectionEntity.class);
        PlayerBrawlerCollectionEntity updatedPlayerBrawler2 = mock(PlayerBrawlerCollectionEntity.class);
        PlayerBrawlerCollectionEntity updatedPlayerBrawler3 = mock(PlayerBrawlerCollectionEntity.class);
        List<PlayerBrawlerCollectionEntity> updatedPlayerBrawlerList = List.of(
                updatedPlayerBrawler1, updatedPlayerBrawler2, updatedPlayerBrawler3);

        given(playerBrawlerUpdateApplier.update(updatedPlayer, playerBrawlerList, playerBrawlerResponseList))
                .willReturn(updatedPlayerBrawlerList);

        BattleCollectionEntity updatedBattle1 = mock(BattleCollectionEntity.class);
        given(updatedBattle1.getBattleTime())
                .willReturn(LocalDateTime.of(2021, 1, 1, 0, 0));
        BattleCollectionEntity updatedBattle2 = mock(BattleCollectionEntity.class);
        given(updatedBattle2.getBattleTime())
                .willReturn(LocalDateTime.of(2021, 1, 2, 0, 0));

        BattleUpdateResult battleUpdateResult1 = new BattleUpdateResult(updatedBattle1, List.of());
        BattleUpdateResult battleUpdateResult2 = new BattleUpdateResult(updatedBattle2, List.of());
        given(battleUpdateApplier.update(updatedPlayer, battleResponseList, null))
                .willReturn(List.of(battleUpdateResult1, battleUpdateResult2));

        // when
        PlayerUpdatedEntity updatedEntity = playerUpdater
                .update(playerToUpdateEntity, playerResponse, battleResponseList);

        // then
        assertThat(updatedEntity.playerEntity()).isEqualTo(updatedPlayer);
        assertThat(updatedEntity.playerBrawlerEntities()).isEqualTo(updatedPlayerBrawlerList);
        assertThat(updatedEntity.battleUpdateResults()).hasSize(2);
        assertThat(updatedEntity.battleUpdateResults().get(0)).isEqualTo(battleUpdateResult1);
        assertThat(updatedEntity.battleUpdateResults().get(1)).isEqualTo(battleUpdateResult2);
        then(updatedPlayer).should().setStatus(PlayerStatus.UPDATED);
        then(updatedPlayer).should().nextUpdateWeight(
                LocalDateTime.now(clock),
                List.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0),
                        LocalDateTime.of(2021, 1, 2, 0, 0)
                )
        );
    }
}