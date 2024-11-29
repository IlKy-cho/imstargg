package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleUpdateResult;
import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class PlayerUpdateJobItemProcessorTest {

    private PlayerUpdateJobItemProcessor playerUpdateJobItemProcessor;

    private Clock clock;

    private BrawlStarsClient brawlStarsClient;

    private PlayerUpdater playerUpdater;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.systemDefault());
        brawlStarsClient = mock(BrawlStarsClient.class);
        playerUpdater = mock(PlayerUpdater.class);

        playerUpdateJobItemProcessor = new PlayerUpdateJobItemProcessor(
                clock,
                brawlStarsClient,
                playerUpdater
        );
    }

    @Test
    void 업데이트를_실행한다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getBrawlStarsTag()).willReturn("testTag");
        given(playerEntity.isNextUpdateCooldownOver(LocalDateTime.now(clock))).willReturn(true);
        PlayerBrawlerCollectionEntity playerBrawler = mock(PlayerBrawlerCollectionEntity.class);

        PlayerToUpdateEntity playerToUpdateEntity = new PlayerToUpdateEntity(
                playerEntity,
                List.of(playerBrawler),
                Optional.empty()
        );

        PlayerResponse playerResponse = mock(PlayerResponse.class);
        given(brawlStarsClient.getPlayerInformation("testTag"))
                .willReturn(playerResponse);

        BattleResponse battleResponse = mock(BattleResponse.class);
        ListResponse<BattleResponse> battleResponseList = new ListResponse<>(List.of(battleResponse), null);
        given(brawlStarsClient.getPlayerRecentBattles("testTag"))
                .willReturn(battleResponseList);

        PlayerCollectionEntity updatedPlayer = mock(PlayerCollectionEntity.class);
        PlayerBrawlerCollectionEntity updatedPlayerBrawler1 = mock(PlayerBrawlerCollectionEntity.class);
        PlayerBrawlerCollectionEntity updatedPlayerBrawler2 = mock(PlayerBrawlerCollectionEntity.class);
        BattleCollectionEntity updatedBattle1 = mock(BattleCollectionEntity.class);
        BattleCollectionEntity updatedBattle2 = mock(BattleCollectionEntity.class);
        BattlePlayerCollectionEntity updatedBattle1Player1 = mock(BattlePlayerCollectionEntity.class);
        BattlePlayerCollectionEntity updatedBattle1Player2 = mock(BattlePlayerCollectionEntity.class);
        BattlePlayerCollectionEntity updatedBattle2Player1 = mock(BattlePlayerCollectionEntity.class);
        BattlePlayerCollectionEntity updatedBattle2Player2 = mock(BattlePlayerCollectionEntity.class);
        PlayerUpdatedEntity playerUpdatedEntity = new PlayerUpdatedEntity(
                updatedPlayer,
                List.of(updatedPlayerBrawler1, updatedPlayerBrawler2),
                List.of(
                        new BattleUpdateResult(updatedBattle1, List.of(updatedBattle1Player1, updatedBattle1Player2)),
                        new BattleUpdateResult(updatedBattle2, List.of(updatedBattle2Player1, updatedBattle2Player2))
                )
        );

        given(playerUpdater.update(playerToUpdateEntity, playerResponse, battleResponseList))
                .willReturn(playerUpdatedEntity);

        // when
        List<Object> results = playerUpdateJobItemProcessor.process(playerToUpdateEntity);
        assertThat(results)
                .hasSize(9)
                .containsExactlyInAnyOrder(
                        updatedPlayer,
                        updatedPlayerBrawler1,
                        updatedPlayerBrawler2,
                        updatedBattle1,
                        updatedBattle1Player1,
                        updatedBattle1Player2,
                        updatedBattle2,
                        updatedBattle2Player1,
                        updatedBattle2Player2
                );
    }

    @Test
    void 업데이트_쿨다운이_지나지_않으면_업데이트하지않는다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.isNextUpdateCooldownOver(LocalDateTime.now(clock))).willReturn(false);
        PlayerToUpdateEntity playerToUpdateEntity = new PlayerToUpdateEntity(
                playerEntity,
                List.of(),
                Optional.empty()
        );

        // when
        List<Object> results = playerUpdateJobItemProcessor.process(playerToUpdateEntity);

        // then
        assertThat(results)
                .hasSize(1)
                .containsExactly(playerEntity);
        then(playerEntity).should().setStatus(PlayerStatus.UPDATED);
    }

    @Test
    void 클라이언트로_정보_조회시_존재하지_않을경우_삭제된_플레이어로_처리한다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getBrawlStarsTag()).willReturn("testTag");
        given(playerEntity.isNextUpdateCooldownOver(LocalDateTime.now(clock))).willReturn(true);
        PlayerToUpdateEntity playerToUpdateEntity = new PlayerToUpdateEntity(
                playerEntity,
                List.of(),
                Optional.empty()
        );

        BrawlStarsClientNotFoundException notFoundException = mock(BrawlStarsClientNotFoundException.class);
        given(brawlStarsClient.getPlayerInformation("testTag"))
                .willThrow(notFoundException);

        // when
        List<Object> results = playerUpdateJobItemProcessor.process(playerToUpdateEntity);

        // then
        assertThat(results)
                .hasSize(1)
                .containsExactly(playerEntity);
        then(playerEntity).should().setStatus(PlayerStatus.DELETED);
    }
}
