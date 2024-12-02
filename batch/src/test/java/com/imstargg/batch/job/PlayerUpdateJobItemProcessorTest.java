package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerDeleter;
import com.imstargg.batch.domain.PlayerToUpdateEntity;
import com.imstargg.batch.domain.PlayerUpdatedEntity;
import com.imstargg.batch.domain.PlayerUpdater;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
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

    private PlayerDeleter playerDeleter;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.systemDefault());
        brawlStarsClient = mock(BrawlStarsClient.class);
        playerUpdater = mock(PlayerUpdater.class);
        playerDeleter = mock(PlayerDeleter.class);

        playerUpdateJobItemProcessor = new PlayerUpdateJobItemProcessor(
                clock,
                brawlStarsClient,
                playerUpdater,
                playerDeleter
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

        PlayerUpdatedEntity playerUpdatedEntity = mock(PlayerUpdatedEntity.class);

        given(playerUpdater.update(playerToUpdateEntity, playerResponse, battleResponseList))
                .willReturn(playerUpdatedEntity);

        // when
        // then
        assertThat(playerUpdateJobItemProcessor.process(playerToUpdateEntity))
                .isEqualTo(playerUpdatedEntity);
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
        var result = playerUpdateJobItemProcessor.process(playerToUpdateEntity);

        // then
        assertThat(result).isNull();
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
        playerUpdateJobItemProcessor.process(playerToUpdateEntity);

        // then
        then(playerDeleter).should().delete(playerEntity);
    }
}
