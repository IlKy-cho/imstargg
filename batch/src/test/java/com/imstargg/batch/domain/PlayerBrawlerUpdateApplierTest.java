package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.core.enums.Brawler;
import com.imstargg.storage.db.core.PlayerBrawlerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PlayerBrawlerUpdateApplierTest {

    private PlayerBrawlerUpdateApplier playerBrawlerUpdateApplier;

    @BeforeEach
    void setUp() {
        playerBrawlerUpdateApplier = new PlayerBrawlerUpdateApplier();
    }

    @Test
    void 브롤러를_업데이트한다() {
        // given
        long playerId = 123;
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(playerId);
        List<PlayerBrawlerCollectionEntity> playerBrawlerEntities = List.of(
                new PlayerBrawlerCollectionEntity(
                        playerEntity,
                        16000000,
                        8,
                        23,
                        449,
                        453,
                        List.of(),
                        List.of(),
                        List.of(Brawler.SHELLY.getGadgets().get(0).getBrawlStarsId())
                )
        );
        List<BrawlerStatResponse> brawlerResponseList = List.of(
                new BrawlerStatResponse(
                        Brawler.SHELLY.getBrawlStarsId(),
                        Brawler.SHELLY.getBrawlStarsName(),
                        11,
                        25,
                        500,
                        500,
                        List.of(
                                new GearStatResponse(
                                        Brawler.SHELLY.getGears().get(0).getBrawlStarsId(),
                                        Brawler.SHELLY.getGears().get(0).getBrawlStarsName(),
                                        Brawler.SHELLY.getGears().get(0).getLevel()

                                )
                        ),
                        List.of(),
                        List.of(
                                new StarPowerResponse(
                                        Brawler.SHELLY.getStarPowers().get(0).getBrawlStarsId(),
                                        Brawler.SHELLY.getStarPowers().get(0).getBrawlStarsName()
                                ),
                                new StarPowerResponse(
                                        Brawler.SHELLY.getStarPowers().get(1).getBrawlStarsId(),
                                        Brawler.SHELLY.getStarPowers().get(1).getBrawlStarsName()
                                )
                        )
                ),
                new BrawlerStatResponse(
                        Brawler.NITA.getBrawlStarsId(),
                        Brawler.NITA.getBrawlStarsName(),
                        1,
                        1,
                        0,
                        0,
                        List.of(),
                        List.of(),
                        List.of()
                )
        );

        // when
        List<PlayerBrawlerCollectionEntity> updatedEntities = playerBrawlerUpdateApplier
                .update(playerEntity, playerBrawlerEntities, brawlerResponseList);

        // then
        assertThat(updatedEntities)
                .describedAs("기존에 없던 브롤러는 새로 추가되어야 한다.")
                .hasSize(2);
        assertThat(updatedEntities.get(0))
                .describedAs("기존 브롤러는 수정되어야 한다.")
                .isEqualTo(playerBrawlerEntities.get(0));
        assertThat(updatedEntities.get(0).getBrawlerBrawlStarsId()).isEqualTo(Brawler.SHELLY.getBrawlStarsId());
        assertThat(updatedEntities.get(0).getPower()).isEqualTo(11);
        assertThat(updatedEntities.get(0).getRank()).isEqualTo(25);
        assertThat(updatedEntities.get(0).getTrophies()).isEqualTo(500);
        assertThat(updatedEntities.get(0).getHighestTrophies()).isEqualTo(500);
        assertThat(updatedEntities.get(0).getGearBrawlStarsIds())
                .describedAs("빈 리스트에 새로 추가될 수 있어야 한다.")
                .hasSize(1);
        assertThat(updatedEntities.get(0).getGearBrawlStarsIds().get(0))
                .isEqualTo(Brawler.SHELLY.getGears().get(0).getBrawlStarsId());
        assertThat(updatedEntities.get(0).getStarPowerBrawlStarsIds())
                .describedAs("비어있지 않은 리스트에 새로 추가될 수 있어야 한다.")
                .hasSize(2);
        assertThat(updatedEntities.get(0).getStarPowerBrawlStarsIds().get(0))
                .isEqualTo(Brawler.SHELLY.getStarPowers().get(0).getBrawlStarsId());
        assertThat(updatedEntities.get(0).getStarPowerBrawlStarsIds().get(1))
                .isEqualTo(Brawler.SHELLY.getStarPowers().get(1).getBrawlStarsId());
        assertThat(updatedEntities.get(0).getGadgetBrawlStarsIds())
                .describedAs("변경사항이 없을 수 있다.")
                .isEmpty();
        assertThat(updatedEntities.get(1).getId())
                .describedAs("추가된 브롤러는 생성되어야 한다.")
                .isNull();
        assertThat(updatedEntities.get(1).getPlayer().getId())
                .describedAs("브롤러가 추가될 때는 인자로 넘겨준 playerId를 그대로 사용해야 한다.")
                .isEqualTo(playerId);
        assertThat(updatedEntities.get(1).getBrawlerBrawlStarsId()).isEqualTo(Brawler.NITA.getBrawlStarsId());
        assertThat(updatedEntities.get(1).getPower()).isEqualTo(1);
        assertThat(updatedEntities.get(1).getRank()).isEqualTo(1);
        assertThat(updatedEntities.get(1).getTrophies()).isZero();
        assertThat(updatedEntities.get(1).getHighestTrophies()).isZero();
        assertThat(updatedEntities.get(1).getGearBrawlStarsIds()).isEmpty();
        assertThat(updatedEntities.get(1).getStarPowerBrawlStarsIds()).isEmpty();
        assertThat(updatedEntities.get(1).getGadgetBrawlStarsIds()).isEmpty();
    }
}