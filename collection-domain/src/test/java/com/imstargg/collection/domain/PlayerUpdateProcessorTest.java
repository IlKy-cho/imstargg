package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.PlayerClubResponse;
import com.imstargg.client.brawlstars.response.PlayerIconResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.storage.db.core.player.PlayerCollectionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerUpdateProcessorTest {

    @InjectMocks
    private PlayerUpdateProcessor playerUpdateProcessor;

    @Mock
    private BrawlStarsClient brawlStarsClient;

    @Test
    void 플레이어를_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntity("#12345");

        PlayerResponse playerResponse = new PlayerResponse(
                "#12345",
                "테스트플레이어",
                "0xFF0000",
                new PlayerIconResponse(1000L),
                500,
                600,
                100,
                10000,
                true,
                1000,
                500,
                300,
                100,
                200,
                new PlayerClubResponse("#CLUB123", "클럽이름"),
                List.of(
                        new BrawlerStatResponse(
                                16000000,
                                "brawler1",
                                9,
                                25,
                                750,
                                800,
                                List.of(new GearStatResponse(1, "gear1", 1)),
                                List.of(new AccessoryResponse(3, "gadget1")),
                                List.of(new StarPowerResponse(2, "starPower1"))
                        ),
                        new BrawlerStatResponse(
                                16000001,
                                "brawler2",
                                10,
                                35,
                                550,
                                600,
                                List.of(new GearStatResponse(4, "gear2", 1), new GearStatResponse(5, "gear3", 1)),
                                List.of(),
                                List.of(new StarPowerResponse(6, "starPower2"))
                        )
                )
        );
        when(brawlStarsClient.getPlayerInformation("#12345")).thenReturn(playerResponse);

        // when
        playerUpdateProcessor.update(playerEntity);

        // then
        assertThat(playerEntity.getBrawlStarsTag()).isEqualTo("#12345");
        assertThat(playerEntity.getName()).isEqualTo("테스트플레이어");
        assertThat(playerEntity.getNameColor()).isEqualTo("0xFF0000");
        assertThat(playerEntity.getIconBrawlStarsId()).isEqualTo(1000L);
        assertThat(playerEntity.getTrophies()).isEqualTo(500);
        assertThat(playerEntity.getHighestTrophies()).isEqualTo(600);
        assertThat(playerEntity.getExpLevel()).isEqualTo(100);
        assertThat(playerEntity.getExpPoints()).isEqualTo(10000);
        assertThat(playerEntity.isQualifiedFromChampionshipChallenge()).isTrue();
        assertThat(playerEntity.getVictories3vs3()).isEqualTo(1000);
        assertThat(playerEntity.getSoloVictories()).isEqualTo(500);
        assertThat(playerEntity.getDuoVictories()).isEqualTo(300);
        assertThat(playerEntity.getBestRoboRumbleTime()).isEqualTo(100);
        assertThat(playerEntity.getBestTimeAsBigBrawler()).isEqualTo(200);
        assertThat(playerEntity.getBrawlStarsClubTag()).isEqualTo("#CLUB123");
        assertThat(playerEntity.getBrawlers().size()).isEqualTo(2);
        assertThat(playerEntity.getBrawlers().get(0).getBrawlerBrawlStarsId()).isEqualTo(16000000);
        assertThat(playerEntity.getBrawlers().get(0).getPower()).isEqualTo(9);
        assertThat(playerEntity.getBrawlers().get(0).getRank()).isEqualTo(25);
        assertThat(playerEntity.getBrawlers().get(0).getTrophies()).isEqualTo(750);
        assertThat(playerEntity.getBrawlers().get(0).getHighestTrophies()).isEqualTo(800);
        assertThat(playerEntity.getBrawlers().get(0).getGearBrawlStarsIds()).containsExactly(1L);
        assertThat(playerEntity.getBrawlers().get(0).getGadgetBrawlStarsIds()).containsExactly(3L);
        assertThat(playerEntity.getBrawlers().get(0).getStarPowerBrawlStarsIds()).containsExactly(2L);
        assertThat(playerEntity.getBrawlers().get(1).getBrawlerBrawlStarsId()).isEqualTo(16000001);
        assertThat(playerEntity.getBrawlers().get(1).getPower()).isEqualTo(10);
        assertThat(playerEntity.getBrawlers().get(1).getRank()).isEqualTo(35);
        assertThat(playerEntity.getBrawlers().get(1).getTrophies()).isEqualTo(550);
        assertThat(playerEntity.getBrawlers().get(1).getHighestTrophies()).isEqualTo(600);
        assertThat(playerEntity.getBrawlers().get(1).getGearBrawlStarsIds()).containsExactly(4L, 5L);
        assertThat(playerEntity.getBrawlers().get(1).getGadgetBrawlStarsIds()).isEmpty();
        assertThat(playerEntity.getBrawlers().get(1).getStarPowerBrawlStarsIds()).containsExactly(6L);
    }
}