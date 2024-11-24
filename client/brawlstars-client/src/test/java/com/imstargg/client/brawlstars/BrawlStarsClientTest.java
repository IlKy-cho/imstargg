package com.imstargg.client.brawlstars;

import com.imstargg.client.brawlstars.request.PagingParam;
import com.imstargg.support.ratelimit.RateLimiter;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BrawlStarsClientTest {

    @InjectMocks
    private BrawlStarsClient brawlstarsClient;

    @Mock
    private BrawlStarsApi brawlstarsApi;

    @Mock
    private RateLimiter rateLimiter;

    @Test
    void 플레이어_전적_조회시_없는_플레이어일_경우_예외를_발생시킨다() {
        // given
        String playerTag = "non-exist-player-tag";
        given(brawlstarsApi.getLogOfRecentBattlesForAPlayer(playerTag))
                .willThrow(FeignException.NotFound.class);

        // when
        // then
        assertThatThrownBy(() -> brawlstarsClient.getPlayerRecentBattles(playerTag))
                .isInstanceOf(BrawlStarsClientNotFoundException.class);
    }

    @Test
    void 플레이어_전적_조회시_호출_제한기를_사용한다() {
        // given
        String playerTag = "player-tag";

        // when
        brawlstarsClient.getPlayerRecentBattles(playerTag);

        // then
        then(rateLimiter).should().acquire();
    }

    @Test
    void 플레이어_정보_조회시_없는_플레이어일_경우_예외를_발생시킨다() {
        // given
        String playerTag = "non-exist-player-tag";
        given(brawlstarsApi.getPlayerInformation(playerTag))
                .willThrow(FeignException.NotFound.class);

        // when
        // then
        assertThatThrownBy(() -> brawlstarsClient.getPlayerInformation(playerTag))
                .isInstanceOf(BrawlStarsClientNotFoundException.class);
    }

    @Test
    void 플레이어_정보_조회시_호출_제한기를_사용한다() {
        // given
        String playerTag = "player-tag";

        // when
        brawlstarsClient.getPlayerInformation(playerTag);

        // then
        then(rateLimiter).should().acquire();
    }

    @Test
    void 브롤러_목록_조회시_기본_페이징_파라미터를_사용한다() {
        // given
        // when
        brawlstarsClient.getBrawlers();

        // then
        then(brawlstarsApi).should().getListOfAvailableBrawlers(PagingParam.DEFAULT);
    }

    @Test
    void 브롤러_목록_조회시_호출_제한기를_사용한다() {
        // given
        // when
        brawlstarsClient.getBrawlers();

        // then
        then(rateLimiter).should().acquire();
    }

}