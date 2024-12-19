package com.imstargg.batch.job.notused;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientNotFoundException;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntityFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerUpdateJobItemProcessorTest {

    @InjectMocks
    private PlayerUpdateJobItemProcessor playerUpdateJobItemProcessor;

    @Mock
    private BrawlStarsClient brawlStarsClient;


    @Test
    void 클라이언트로_정보_조회시_존재하지_않을경우_삭제된_플레이어로_처리한다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntityFixture()
                .build();

        BrawlStarsClientNotFoundException notFoundException = mock(BrawlStarsClientNotFoundException.class);
        given(brawlStarsClient.getPlayerInformation(playerEntity.getBrawlStarsTag()))
                .willThrow(notFoundException);

        // when
        playerUpdateJobItemProcessor.process(playerEntity);

        // then
        assertThat(playerEntity.getStatus()).isEqualTo(PlayerStatus.DELETED);
    }
}
