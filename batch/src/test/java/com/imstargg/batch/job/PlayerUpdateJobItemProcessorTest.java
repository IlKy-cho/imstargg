package com.imstargg.batch.job;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerUpdateJobItemProcessorTest {

    private PlayerUpdateJobItemProcessor playerUpdateJobItemProcessor;

    private Clock clock;

    private BrawlStarsClient brawlStarsClient;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.systemDefault());
        brawlStarsClient = mock(BrawlStarsClient.class);

        playerUpdateJobItemProcessor = new PlayerUpdateJobItemProcessor(
                clock,
                brawlStarsClient
        );
    }

    @Test
    void 업데이트_쿨다운이_지나지_않으면_업데이트하지않는다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.isNextUpdateCooldownOver(LocalDateTime.now(clock))).willReturn(false);

        // when
        var result = playerUpdateJobItemProcessor.process(playerEntity);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 클라이언트로_정보_조회시_존재하지_않을경우_삭제된_플레이어로_처리한다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getBrawlStarsTag()).willReturn("testTag");
        given(playerEntity.isNextUpdateCooldownOver(LocalDateTime.now(clock))).willReturn(true);

        BrawlStarsClientNotFoundException notFoundException = mock(BrawlStarsClientNotFoundException.class);
        given(brawlStarsClient.getPlayerInformation("testTag"))
                .willThrow(notFoundException);

        // when
        playerUpdateJobItemProcessor.process(playerEntity);

        // then
        then(playerEntity).should().delete();
    }
}
