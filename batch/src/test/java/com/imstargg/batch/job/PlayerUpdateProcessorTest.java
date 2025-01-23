package com.imstargg.batch.job;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
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
class PlayerUpdateProcessorTest {

    @InjectMocks
    private PlayerUpdateProcessor playerUpdateProcessor;

    @Mock
    private BrawlStarsClient brawlStarsClient;


    @Test
    void 클라이언트로_정보_조회시_존재하지_않을경우_삭제된_플레이어로_처리한다() throws Exception {
        // given
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntityFixture()
                .build();

        BrawlStarsClientException.NotFound notFoundException = mock(BrawlStarsClientException.NotFound.class);
        given(brawlStarsClient.getPlayerInformation(playerEntity.getBrawlStarsTag()))
                .willThrow(notFoundException);

        // when
        playerUpdateProcessor.process(playerEntity);

        // then
        assertThat(playerEntity.getStatus()).isEqualTo(PlayerStatus.DELETED);
    }
}
