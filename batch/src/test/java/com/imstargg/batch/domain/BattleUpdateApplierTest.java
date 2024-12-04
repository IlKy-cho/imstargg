package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.client.brawlstars.response.BattleResultResponse;
import com.imstargg.client.brawlstars.response.EventResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.core.enums.BattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.Brawler;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattlePlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BattleUpdateApplierTest {

    private BattleUpdateApplier battleUpdateApplier;

    @BeforeEach
    void setUp() {
        battleUpdateApplier = new BattleUpdateApplier();
    }

    @Test
    void 최근_전적_보다_이전_배틀은_필터링되고_가장_최신_전적만_latest상태로_표시한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        LocalDateTime latestBattleTime = LocalDateTime.of(2024, 12, 1, 0, 0, 0);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 0, 0, 0),
                        new EventResponse(
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getBrawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMode().getName(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.KNOCKOUT.getName(),
                                BattleType.SOLO_RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                null,
                                null,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.TICK.getBrawlStarsId(),
                                                                Brawler.TICK.getBrawlStarsName(),
                                                                10,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BUSTER.getBrawlStarsId(),
                                                                Brawler.BUSTER.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPROUT.getBrawlStarsId(),
                                                                Brawler.SPROUT.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.MOE.getBrawlStarsId(),
                                                                Brawler.MOE.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SQUEAK.getBrawlStarsId(),
                                                                Brawler.SQUEAK.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                ),
                new BattleResponse(
                        LocalDateTime.of(2024, 12, 1, 0, 0, 0),
                        new EventResponse(
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getBrawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMode().getName(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.KNOCKOUT.getName(),
                                BattleType.SOLO_RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                null,
                                null,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.TICK.getBrawlStarsId(),
                                                                Brawler.TICK.getBrawlStarsName(),
                                                                10,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BUSTER.getBrawlStarsId(),
                                                                Brawler.BUSTER.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPROUT.getBrawlStarsId(),
                                                                Brawler.SPROUT.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.MOE.getBrawlStarsId(),
                                                                Brawler.MOE.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SQUEAK.getBrawlStarsId(),
                                                                Brawler.SQUEAK.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                ),
                new BattleResponse(
                        LocalDateTime.of(2024, 12, 29, 0, 0, 0),
                        new EventResponse(
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getBrawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMode().getName(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.KNOCKOUT.getName(),
                                BattleType.SOLO_RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                null,
                                null,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.TICK.getBrawlStarsId(),
                                                                Brawler.TICK.getBrawlStarsName(),
                                                                10,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BUSTER.getBrawlStarsId(),
                                                                Brawler.BUSTER.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPROUT.getBrawlStarsId(),
                                                                Brawler.SPROUT.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.MOE.getBrawlStarsId(),
                                                                Brawler.MOE.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SQUEAK.getBrawlStarsId(),
                                                                Brawler.SQUEAK.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), latestBattleTime);

        // then
        assertThat(results).hasSize(1);

        var battleEntity = results.get(0);
        assertThat(battleEntity.getBattleTime()).isEqualTo(LocalDateTime.of(2024, 12, 29, 0, 0, 0));
    }

    @Test
    void 솔로랭크_녹아웃을_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getBrawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMode().getName(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.KNOCKOUT.getName(),
                                BattleType.SOLO_RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                null,
                                null,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.TICK.getBrawlStarsId(),
                                                                Brawler.TICK.getBrawlStarsName(),
                                                                10,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BUSTER.getBrawlStarsId(),
                                                                Brawler.BUSTER.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPROUT.getBrawlStarsId(),
                                                                Brawler.SPROUT.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.MOE.getBrawlStarsId(),
                                                                Brawler.MOE.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SQUEAK.getBrawlStarsId(),
                                                                Brawler.SQUEAK.getBrawlStarsName(),
                                                                11,
                                                                11,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        var battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams(), battleEntity);
    }

    @Test
    void 듀오쇼다운을_업데이트_한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.getBrawlStarsId(),
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.getMode().getName(),
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.DUO_SHOWDOWN.getName(),
                                BattleType.RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                5,
                                -4,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GALE.getBrawlStarsId(),
                                                                Brawler.GALE.getBrawlStarsName(),
                                                                9,
                                                                476,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GENE.getBrawlStarsId(),
                                                                Brawler.GENE.getBrawlStarsName(),
                                                                11,
                                                                619,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BULL.getBrawlStarsId(),
                                                                Brawler.BULL.getBrawlStarsName(),
                                                                8,
                                                                606,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BYRON.getBrawlStarsId(),
                                                                Brawler.BYRON.getBrawlStarsName(),
                                                                9,
                                                                601,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SHADE.getBrawlStarsId(),
                                                                Brawler.SHADE.getBrawlStarsName(),
                                                                10,
                                                                640,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.COLT.getBrawlStarsId(),
                                                                Brawler.COLT.getBrawlStarsName(),
                                                                11,
                                                                602,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player7",
                                                        "player7",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.ANGELO.getBrawlStarsId(),
                                                                Brawler.ANGELO.getBrawlStarsName(),
                                                                11,
                                                                609,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player8",
                                                        "player8",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.PAM.getBrawlStarsId(),
                                                                Brawler.PAM.getBrawlStarsName(),
                                                                9,
                                                                621,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player9",
                                                        "player9",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.LILY.getBrawlStarsId(),
                                                                Brawler.LILY.getBrawlStarsName(),
                                                                11,
                                                                606,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player10",
                                                        "player10",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EDGAR.getBrawlStarsId(),
                                                                Brawler.EDGAR.getBrawlStarsName(),
                                                                9,
                                                                623,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        List<BattlePlayerCollectionEntity> battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams(), battleEntity);
    }

    @Test
    void 솔로_쇼다운을_업데이트한다() {
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getBrawlStarsId(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getMode().getName(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.SOLO_SHOWDOWN.getName(),
                                BattleType.RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                1,
                                11,
                                null,
                                null,
                                List.of(
                                        new BattleResultPlayerResponse(
                                                "#player1",
                                                "player1",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.GALE.getBrawlStarsId(),
                                                        Brawler.GALE.getBrawlStarsName(),
                                                        9,
                                                        476,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player2",
                                                "player2",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.GENE.getBrawlStarsId(),
                                                        Brawler.GENE.getBrawlStarsName(),
                                                        11,
                                                        619,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player3",
                                                "player3",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.BULL.getBrawlStarsId(),
                                                        Brawler.BULL.getBrawlStarsName(),
                                                        8,
                                                        606,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player4",
                                                "player4",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.BYRON.getBrawlStarsId(),
                                                        Brawler.BYRON.getBrawlStarsName(),
                                                        9,
                                                        601,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player5",
                                                "player5",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.SHADE.getBrawlStarsId(),
                                                        Brawler.SHADE.getBrawlStarsName(),
                                                        10,
                                                        640,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player6",
                                                "player6",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.COLT.getBrawlStarsId(),
                                                        Brawler.COLT.getBrawlStarsName(),
                                                        11,
                                                        602,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player7",
                                                "player7",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.ANGELO.getBrawlStarsId(),
                                                        Brawler.ANGELO.getBrawlStarsName(),
                                                        11,
                                                        609,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player8",
                                                "player8",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.PAM.getBrawlStarsId(),
                                                        Brawler.PAM.getBrawlStarsName(),
                                                        9,
                                                        621,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player9",
                                                "player9",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.LILY.getBrawlStarsId(),
                                                        Brawler.LILY.getBrawlStarsName(),
                                                        11,
                                                        606,
                                                        null
                                                ),
                                                null
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player10",
                                                "player10",
                                                new BattleResultBrawlerResponse(
                                                        Brawler.EDGAR.getBrawlStarsId(),
                                                        Brawler.EDGAR.getBrawlStarsName(),
                                                        9,
                                                        623,
                                                        null
                                                ),
                                                null
                                        )
                                )
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        List<BattlePlayerCollectionEntity> battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattlePlayers(battlePlayerEntities, battleResponse.battle().players(), battleEntity);
    }

    @Test
    void 듀얼을_업데이트한다() {
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getBrawlStarsId(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getMode().getName(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.SOLO_SHOWDOWN.getName(),
                                BattleType.RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                117,
                                1,
                                11,
                                null,
                                null,
                                List.of(
                                        new BattleResultPlayerResponse(
                                                "#player1",
                                                "player1",
                                                null,
                                                List.of(
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                89,
                                                                4
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.LILY.getBrawlStarsId(),
                                                                Brawler.LILY.getBrawlStarsName(),
                                                                11,
                                                                581,
                                                                4
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GUS.getBrawlStarsId(),
                                                                Brawler.GUS.getBrawlStarsName(),
                                                                11,
                                                                458,
                                                                4
                                                        )
                                                )
                                        ),
                                        new BattleResultPlayerResponse(
                                                "#player2",
                                                "player2",
                                                null,
                                                List.of(
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.COLT.getBrawlStarsId(),
                                                                Brawler.COLT.getBrawlStarsName(),
                                                                11,
                                                                586,
                                                                0
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EIGHT_BIT.getBrawlStarsId(),
                                                                Brawler.EIGHT_BIT.getBrawlStarsName(),
                                                                11,
                                                                590,
                                                                0
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BULL.getBrawlStarsId(),
                                                                Brawler.BULL.getBrawlStarsName(),
                                                                11,
                                                                587,
                                                                0
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        List<BattlePlayerCollectionEntity> battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattlePlayers(battlePlayerEntities, battleResponse.battle().players(), battleEntity);
    }

    @Test
    void 트리오_쇼다운을_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.getBrawlStarsId(),
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.getMode().getName(),
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.TRIO_SHOWDOWN.getName(),
                                BattleType.RANKED.getCode(),
                                null,
                                null,
                                4,
                                null,
                                null,
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GUS.getBrawlStarsId(),
                                                                Brawler.GUS.getBrawlStarsName(),
                                                                11,
                                                                575,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EDGAR.getBrawlStarsId(),
                                                                Brawler.EDGAR.getBrawlStarsName(),
                                                                11,
                                                                807,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BUSTER.getBrawlStarsId(),
                                                                Brawler.BUSTER.getBrawlStarsName(),
                                                                7,
                                                                210,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EDGAR.getBrawlStarsId(),
                                                                Brawler.EDGAR.getBrawlStarsName(),
                                                                11,
                                                                690,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPIKE.getBrawlStarsId(),
                                                                Brawler.SPIKE.getBrawlStarsName(),
                                                                9,
                                                                507,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BEA.getBrawlStarsId(),
                                                                Brawler.BEA.getBrawlStarsName(),
                                                                11,
                                                                529,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player7",
                                                        "player7",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BROCK.getBrawlStarsId(),
                                                                Brawler.BROCK.getBrawlStarsName(),
                                                                11,
                                                                529,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player8",
                                                        "player8",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.FANG.getBrawlStarsId(),
                                                                Brawler.FANG.getBrawlStarsName(),
                                                                11,
                                                                649,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player9",
                                                        "player9",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BEA.getBrawlStarsId(),
                                                                Brawler.BEA.getBrawlStarsName(),
                                                                9,
                                                                419,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player10",
                                                        "player10",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.SPIKE.getBrawlStarsId(),
                                                                Brawler.SPIKE.getBrawlStarsName(),
                                                                11,
                                                                497,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player11",
                                                        "player11",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.LILY.getBrawlStarsId(),
                                                                Brawler.LILY.getBrawlStarsName(),
                                                                9,
                                                                507,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player12",
                                                        "player12",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.MICO.getBrawlStarsId(),
                                                                Brawler.MICO.getBrawlStarsName(),
                                                                11,
                                                                500,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        List<BattlePlayerCollectionEntity> battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams(), battleEntity);
    }

    @Test
    void 녹아웃_5대5를_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = mock(PlayerCollectionEntity.class);
        given(playerEntity.getId()).willReturn(123L);
        given(playerEntity.getTrophies()).willReturn(500);

        List<BattleResponse> battleResponseList = List.of(
                new BattleResponse(
                        LocalDateTime.of(2024, 11, 29, 11, 0, 0),
                        new EventResponse(
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.getBrawlStarsId(),
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.getMode().getName(),
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.getMap().getName()
                        ),
                        new BattleResultResponse(
                                BattleEventMode.KNOCKOUT.getName(),
                                BattleType.RANKED.getCode(),
                                BattleResult.VICTORY.getCode(),
                                144,
                                null,
                                12,
                                new BattleResultPlayerResponse(
                                        "#player1",
                                        "player1",
                                        new BattleResultBrawlerResponse(
                                                Brawler.JUJU.getBrawlStarsId(),
                                                Brawler.JUJU.getBrawlStarsName(),
                                                11,
                                                93,
                                                null
                                        ),
                                        null
                                ),
                                List.of(
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player1",
                                                        "player1",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.JUJU.getBrawlStarsId(),
                                                                Brawler.JUJU.getBrawlStarsName(),
                                                                11,
                                                                93,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player2",
                                                        "player2",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.LOLA.getBrawlStarsId(),
                                                                Brawler.LOLA.getBrawlStarsName(),
                                                                1,
                                                                17,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player3",
                                                        "player3",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.PENNY.getBrawlStarsId(),
                                                                Brawler.PENNY.getBrawlStarsName(),
                                                                4,
                                                                96,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player4",
                                                        "player4",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.DOUG.getBrawlStarsId(),
                                                                Brawler.DOUG.getBrawlStarsName(),
                                                                4,
                                                                0,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player5",
                                                        "player5",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.POCO.getBrawlStarsId(),
                                                                Brawler.POCO.getBrawlStarsName(),
                                                                1,
                                                                17,
                                                                null
                                                        ),
                                                        null
                                                )
                                        ),
                                        List.of(
                                                new BattleResultPlayerResponse(
                                                        "#player6",
                                                        "player6",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BULL.getBrawlStarsId(),
                                                                Brawler.BULL.getBrawlStarsName(),
                                                                4,
                                                                56,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player7",
                                                        "player7",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.WILLOW.getBrawlStarsId(),
                                                                Brawler.WILLOW.getBrawlStarsName(),
                                                                3,
                                                                56,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player8",
                                                        "player8",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GROM.getBrawlStarsId(),
                                                                Brawler.GROM.getBrawlStarsName(),
                                                                5,
                                                                56,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player9",
                                                        "player9",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EIGHT_BIT.getBrawlStarsId(),
                                                                Brawler.EIGHT_BIT.getBrawlStarsName(),
                                                                4,
                                                                48,
                                                                null
                                                        ),
                                                        null
                                                ),
                                                new BattleResultPlayerResponse(
                                                        "#player10",
                                                        "player10",
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.ROSA.getBrawlStarsId(),
                                                                Brawler.ROSA.getBrawlStarsName(),
                                                                3,
                                                                48,
                                                                null
                                                        ),
                                                        null
                                                )
                                        )
                                ),
                                null
                        )
                )
        );

        // when
        var results = battleUpdateApplier.update(
                playerEntity, new ListResponse<>(battleResponseList, null), null);

        // then
        assertThat(results).hasSize(1);

        BattleCollectionEntity battleEntity = results.get(0);
        BattleResponse battleResponse = battleResponseList.get(0);
        assertBattle(battleEntity, battleResponse, playerEntity);

        List<BattlePlayerCollectionEntity> battlePlayerEntities = battleEntity.getBattlePlayers();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams(), battleEntity);
    }

    private void assertBattle(
            BattleCollectionEntity actual, BattleResponse expected, PlayerCollectionEntity playerEntity
    ) {
        assertThat(actual.getId()).isNull();
        var battleKeyBuilder = new BattleKeyBuilder(expected.battleTime());
        Optional.ofNullable(expected.battle().players())
                .or(() -> Optional.ofNullable(expected.battle().teams())
                        .map(teams -> teams.stream().flatMap(List::stream).toList())
                ).orElseThrow()
                .forEach(player -> battleKeyBuilder.addPlayerTag(player.tag()));
        assertThat(actual.getBattleKey()).isEqualTo(
                battleKeyBuilder.build()
        );
        assertThat(actual.getBattleTime()).isEqualTo(expected.battleTime());
        assertThat(actual.getEvent().getEventBrawlStarsId()).isEqualTo(expected.event().id());
        assertThat(actual.getEvent().getMode()).isEqualTo(expected.event().mode());
        assertThat(actual.getEvent().getMap()).isEqualTo(expected.event().map());
        assertThat(actual.getMode()).isEqualTo(expected.battle().mode());
        assertThat(actual.getType()).isEqualTo(expected.battle().type());
        assertThat(actual.getResult()).isEqualTo(expected.battle().result());
        assertThat(actual.getDuration()).isEqualTo(expected.battle().duration());
        assertThat(actual.getStarPlayerBrawlStarsTag()).isEqualTo(
                Optional.ofNullable(expected.battle().starPlayer())
                        .map(BattleResultPlayerResponse::tag)
                        .orElse(null)
        );
        assertThat(actual.getPlayer().getPlayer().getId()).isEqualTo(playerEntity.getId());
        assertThat(actual.getPlayer().getRank()).isEqualTo(expected.battle().rank());
        assertThat(actual.getPlayer().getTrophyChange()).isEqualTo(expected.battle().trophyChange());
        assertThat(actual.getPlayer().getTrophySnapshot()).isEqualTo(playerEntity.getTrophies());
    }

    private void assertBattleTeams(List<BattlePlayerCollectionEntity> actuals, List<List<BattleResultPlayerResponse>> expecteds, BattleCollectionEntity battleEntity) {
        int expectedSize = 0;
        for (int teamIdx = 0; teamIdx < expecteds.size(); teamIdx++) {
            for (int playerIdx = 0; playerIdx < expecteds.get(teamIdx).size(); playerIdx++) {
                BattleResultPlayerResponse playerResponse = expecteds.get(teamIdx).get(playerIdx);
                if (playerResponse.brawler() != null) {
                    assertBattlePlayerWithABrawler(
                            actuals.get(expectedSize),
                            playerResponse,
                            battleEntity,
                            teamIdx,
                            playerIdx
                    );
                    expectedSize++;
                } else {
                    for (int playerBrawlerIdx = 0; playerBrawlerIdx < playerResponse.brawlers().size(); playerBrawlerIdx++) {
                        assertBattlePlayerWithBrawlers(
                                actuals.get(expectedSize),
                                playerResponse,
                                playerBrawlerIdx,
                                battleEntity,
                                teamIdx,
                                playerIdx
                        );
                        expectedSize++;
                    }
                }
            }
        }

        assertThat(actuals).hasSize(expectedSize);
    }

    private void assertBattlePlayers(
            List<BattlePlayerCollectionEntity> actuals,
            List<BattleResultPlayerResponse> expecteds,
            BattleCollectionEntity battleEntity
    ) {
        int expectedSize = 0;
        for (int i = 0; i < expecteds.size(); i++) {
            BattleResultPlayerResponse playerResponse = expecteds.get(i);
            if (playerResponse.brawler() != null) {
                assertBattlePlayerWithABrawler(actuals.get(i), playerResponse, battleEntity, i, 0);
                expectedSize += 1;
            } else {
                for (int playerBrawlerIdx = 0; playerBrawlerIdx < playerResponse.brawlers().size(); playerBrawlerIdx++) {
                    assertBattlePlayerWithBrawlers(actuals.get(expectedSize), playerResponse, playerBrawlerIdx, battleEntity, i, 0);
                    expectedSize += 1;
                }
            }
        }
        assertThat(actuals).hasSize(expectedSize);
    }

    private void assertBattlePlayerWithABrawler(
            BattlePlayerCollectionEntity actual,
            BattleResultPlayerResponse expected,
            BattleCollectionEntity battleEntity,
            int teamIdx, int playerIdx
    ) {
        assertThat(actual.getId()).isNull();
        assertThat(actual.getBattle()).isEqualTo(battleEntity);
        assertThat(actual.getBrawlStarsTag()).isEqualTo(expected.tag());
        assertThat(actual.getName()).isEqualTo(expected.name());
        assertThat(actual.getTeamIdx()).isEqualTo(teamIdx);
        assertThat(actual.getPlayerIdx()).isEqualTo(playerIdx);
        assertThat(actual.getBrawler().getBrawlStarsId()).isEqualTo(expected.brawler().id());
        assertThat(actual.getBrawler().getName()).isEqualTo(expected.brawler().name());
        assertThat(actual.getBrawler().getPower()).isEqualTo(expected.brawler().power());
        assertThat(actual.getBrawler().getTrophies()).isEqualTo(expected.brawler().trophies());
        assertThat(actual.getBrawler().getTrophyChange()).isEqualTo(expected.brawler().trophyChange());
    }

    private void assertBattlePlayerWithBrawlers(
            BattlePlayerCollectionEntity actual,
            BattleResultPlayerResponse expected,
            int playerBrawlerIdx,
            BattleCollectionEntity battleEntity,
            int teamIdx, int playerIdx
    ) {
        assertThat(actual.getId()).isNull();
        assertThat(actual.getBattle()).isEqualTo(battleEntity);
        assertThat(actual.getBrawlStarsTag()).isEqualTo(expected.tag());
        assertThat(actual.getName()).isEqualTo(expected.name());
        assertThat(actual.getTeamIdx()).isEqualTo(teamIdx);
        assertThat(actual.getPlayerIdx()).isEqualTo(playerIdx);
        assertThat(actual.getBrawler().getBrawlStarsId()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).id());
        assertThat(actual.getBrawler().getName()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).name());
        assertThat(actual.getBrawler().getPower()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).power());
        assertThat(actual.getBrawler().getTrophies()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).trophies());
        assertThat(actual.getBrawler().getTrophyChange()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).trophyChange());
    }
}