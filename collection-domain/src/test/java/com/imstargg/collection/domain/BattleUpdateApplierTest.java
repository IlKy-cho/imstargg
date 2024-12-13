package com.imstargg.collection.domain;

import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.client.brawlstars.response.BattleResultResponse;
import com.imstargg.client.brawlstars.response.EventResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
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

    private record BattleEvent(
            long brawlStarsId,
            String mode,
            String map
    ) {

        static final BattleEvent SOLO_SHOWDOWN_FLYING_FANTASIES = new BattleEvent(10000001, "soloShowdown", "Flying Fantasies");
        static final BattleEvent DUO_SHOWDOWN_FLYING_FANTASIES = new BattleEvent(10000002, "duoShowdown", "Flying Fantasies");
        static final BattleEvent KNOCKOUT_FLARING_PHOENIX = new BattleEvent(10000003, "knockout", "Flaring Phoenix");
        static final BattleEvent TRIO_SHOWDOWN_RING_O_BRAWLNG = new BattleEvent(10000004, "trioShowdown", "Ring 'o Brawlin");
        static final BattleEvent KNOCKOUT_5VS5_SIZZLING_CHAMBERS = new BattleEvent(10000005, "knockout5V5", "Sizzling Chambers");
    }

    private record Brawler(
            long brawlStarsId,
            String brawlStarsName
    ) {
        static final Brawler JUJU = new Brawler(10000001, "Juju");
        static final Brawler TICK = new Brawler(10000002, "Tick");
        static final Brawler BUSTER = new Brawler(10000003, "Buster");
        static final Brawler SPROUT = new Brawler(10000004, "Sprout");
        static final Brawler MOE = new Brawler(10000005, "Moe");
        static final Brawler SQUEAK = new Brawler(10000006, "Squeak");
        static final Brawler GALE = new Brawler(10000007, "Gale");
        static final Brawler GENE = new Brawler(10000008, "Gene");
        static final Brawler BULL = new Brawler(10000009, "Bull");
        static final Brawler BYRON = new Brawler(10000010, "Byron");
        static final Brawler SHADE = new Brawler(10000011, "Shade");
        static final Brawler COLT = new Brawler(10000012, "Colt");
        static final Brawler ANGELO = new Brawler(10000013, "Angelo");
        static final Brawler PAM = new Brawler(10000014, "Pam");
        static final Brawler LILY = new Brawler(10000015, "Lily");
        static final Brawler EDGAR = new Brawler(10000016, "Edgar");
        static final Brawler GUS = new Brawler(10000017, "Gus");
        static final Brawler EIGHT_BIT = new Brawler(10000018, "8-BIT");
        static final Brawler SPIKE = new Brawler(10000018, "Spike");
        static final Brawler BEA = new Brawler(10000018, "Bea");
        static final Brawler BROCK = new Brawler(10000018, "Brock");
        static final Brawler FANG = new Brawler(10000018, "Fang");
        static final Brawler MICO = new Brawler(10000018, "Mico");
        static final Brawler LOLA = new Brawler(10000018, "Lola");
        static final Brawler PENNY = new Brawler(10000018, "Penny");
        static final Brawler DOUG = new Brawler(10000018, "Doug");
        static final Brawler POCO = new Brawler(10000018, "Poco");
        static final Brawler WILLOW = new Brawler(10000018, "Willow");
        static final Brawler GROM = new Brawler(10000018, "Grom");
        static final Brawler ROSA = new Brawler(10000018, "Rosa");
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
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.brawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.mode(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.map()
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.TICK.brawlStarsId(),
                                                                Brawler.TICK.brawlStarsName(),
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
                                                                Brawler.BUSTER.brawlStarsId(),
                                                                Brawler.BUSTER.brawlStarsName(),
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
                                                                Brawler.SPROUT.brawlStarsId(),
                                                                Brawler.SPROUT.brawlStarsName(),
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
                                                                Brawler.MOE.brawlStarsId(),
                                                                Brawler.MOE.brawlStarsName(),
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
                                                                Brawler.SQUEAK.brawlStarsId(),
                                                                Brawler.SQUEAK.brawlStarsName(),
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
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.brawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.mode(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.map()
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.TICK.brawlStarsId(),
                                                                Brawler.TICK.brawlStarsName(),
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
                                                                Brawler.BUSTER.brawlStarsId(),
                                                                Brawler.BUSTER.brawlStarsName(),
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
                                                                Brawler.SPROUT.brawlStarsId(),
                                                                Brawler.SPROUT.brawlStarsName(),
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
                                                                Brawler.MOE.brawlStarsId(),
                                                                Brawler.MOE.brawlStarsName(),
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
                                                                Brawler.SQUEAK.brawlStarsId(),
                                                                Brawler.SQUEAK.brawlStarsName(),
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
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.brawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.mode(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.map()
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.TICK.brawlStarsId(),
                                                                Brawler.TICK.brawlStarsName(),
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
                                                                Brawler.BUSTER.brawlStarsId(),
                                                                Brawler.BUSTER.brawlStarsName(),
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
                                                                Brawler.SPROUT.brawlStarsId(),
                                                                Brawler.SPROUT.brawlStarsName(),
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
                                                                Brawler.MOE.brawlStarsId(),
                                                                Brawler.MOE.brawlStarsName(),
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
                                                                Brawler.SQUEAK.brawlStarsId(),
                                                                Brawler.SQUEAK.brawlStarsName(),
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
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.brawlStarsId(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.mode(),
                                BattleEvent.KNOCKOUT_FLARING_PHOENIX.map()
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.TICK.brawlStarsId(),
                                                                Brawler.TICK.brawlStarsName(),
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
                                                                Brawler.BUSTER.brawlStarsId(),
                                                                Brawler.BUSTER.brawlStarsName(),
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
                                                                Brawler.SPROUT.brawlStarsId(),
                                                                Brawler.SPROUT.brawlStarsName(),
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
                                                                Brawler.MOE.brawlStarsId(),
                                                                Brawler.MOE.brawlStarsName(),
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
                                                                Brawler.SQUEAK.brawlStarsId(),
                                                                Brawler.SQUEAK.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams());
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
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.brawlStarsId(),
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.mode(),
                                BattleEvent.DUO_SHOWDOWN_FLYING_FANTASIES.map()
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
                                                                Brawler.GALE.brawlStarsId(),
                                                                Brawler.GALE.brawlStarsName(),
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
                                                                Brawler.GENE.brawlStarsId(),
                                                                Brawler.GENE.brawlStarsName(),
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
                                                                Brawler.BULL.brawlStarsId(),
                                                                Brawler.BULL.brawlStarsName(),
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
                                                                Brawler.BYRON.brawlStarsId(),
                                                                Brawler.BYRON.brawlStarsName(),
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
                                                                Brawler.SHADE.brawlStarsId(),
                                                                Brawler.SHADE.brawlStarsName(),
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
                                                                Brawler.COLT.brawlStarsId(),
                                                                Brawler.COLT.brawlStarsName(),
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
                                                                Brawler.ANGELO.brawlStarsId(),
                                                                Brawler.ANGELO.brawlStarsName(),
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
                                                                Brawler.PAM.brawlStarsId(),
                                                                Brawler.PAM.brawlStarsName(),
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
                                                                Brawler.LILY.brawlStarsId(),
                                                                Brawler.LILY.brawlStarsName(),
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
                                                                Brawler.EDGAR.brawlStarsId(),
                                                                Brawler.EDGAR.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams());
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
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.brawlStarsId(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.mode(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.map()
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
                                                        Brawler.GALE.brawlStarsId(),
                                                        Brawler.GALE.brawlStarsName(),
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
                                                        Brawler.GENE.brawlStarsId(),
                                                        Brawler.GENE.brawlStarsName(),
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
                                                        Brawler.BULL.brawlStarsId(),
                                                        Brawler.BULL.brawlStarsName(),
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
                                                        Brawler.BYRON.brawlStarsId(),
                                                        Brawler.BYRON.brawlStarsName(),
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
                                                        Brawler.SHADE.brawlStarsId(),
                                                        Brawler.SHADE.brawlStarsName(),
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
                                                        Brawler.COLT.brawlStarsId(),
                                                        Brawler.COLT.brawlStarsName(),
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
                                                        Brawler.ANGELO.brawlStarsId(),
                                                        Brawler.ANGELO.brawlStarsName(),
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
                                                        Brawler.PAM.brawlStarsId(),
                                                        Brawler.PAM.brawlStarsName(),
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
                                                        Brawler.LILY.brawlStarsId(),
                                                        Brawler.LILY.brawlStarsName(),
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
                                                        Brawler.EDGAR.brawlStarsId(),
                                                        Brawler.EDGAR.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattlePlayers(battlePlayerEntities, battleResponse.battle().players());
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
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.brawlStarsId(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.mode(),
                                BattleEvent.SOLO_SHOWDOWN_FLYING_FANTASIES.map()
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
                                                                11,
                                                                89,
                                                                4
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.LILY.brawlStarsId(),
                                                                Brawler.LILY.brawlStarsName(),
                                                                11,
                                                                581,
                                                                4
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.GUS.brawlStarsId(),
                                                                Brawler.GUS.brawlStarsName(),
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
                                                                Brawler.COLT.brawlStarsId(),
                                                                Brawler.COLT.brawlStarsName(),
                                                                11,
                                                                586,
                                                                0
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.EIGHT_BIT.brawlStarsId(),
                                                                Brawler.EIGHT_BIT.brawlStarsName(),
                                                                11,
                                                                590,
                                                                0
                                                        ),
                                                        new BattleResultBrawlerResponse(
                                                                Brawler.BULL.brawlStarsId(),
                                                                Brawler.BULL.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattlePlayers(battlePlayerEntities, battleResponse.battle().players());
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
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.brawlStarsId(),
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.mode(),
                                BattleEvent.TRIO_SHOWDOWN_RING_O_BRAWLNG.map()
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
                                                                Brawler.GUS.brawlStarsId(),
                                                                Brawler.GUS.brawlStarsName(),
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
                                                                Brawler.EDGAR.brawlStarsId(),
                                                                Brawler.EDGAR.brawlStarsName(),
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
                                                                Brawler.BUSTER.brawlStarsId(),
                                                                Brawler.BUSTER.brawlStarsName(),
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
                                                                Brawler.EDGAR.brawlStarsId(),
                                                                Brawler.EDGAR.brawlStarsName(),
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
                                                                Brawler.SPIKE.brawlStarsId(),
                                                                Brawler.SPIKE.brawlStarsName(),
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
                                                                Brawler.BEA.brawlStarsId(),
                                                                Brawler.BEA.brawlStarsName(),
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
                                                                Brawler.BROCK.brawlStarsId(),
                                                                Brawler.BROCK.brawlStarsName(),
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
                                                                Brawler.FANG.brawlStarsId(),
                                                                Brawler.FANG.brawlStarsName(),
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
                                                                Brawler.BEA.brawlStarsId(),
                                                                Brawler.BEA.brawlStarsName(),
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
                                                                Brawler.SPIKE.brawlStarsId(),
                                                                Brawler.SPIKE.brawlStarsName(),
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
                                                                Brawler.LILY.brawlStarsId(),
                                                                Brawler.LILY.brawlStarsName(),
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
                                                                Brawler.MICO.brawlStarsId(),
                                                                Brawler.MICO.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams());
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
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.brawlStarsId(),
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.mode(),
                                BattleEvent.KNOCKOUT_5VS5_SIZZLING_CHAMBERS.map()
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
                                                Brawler.JUJU.brawlStarsId(),
                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.JUJU.brawlStarsId(),
                                                                Brawler.JUJU.brawlStarsName(),
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
                                                                Brawler.LOLA.brawlStarsId(),
                                                                Brawler.LOLA.brawlStarsName(),
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
                                                                Brawler.PENNY.brawlStarsId(),
                                                                Brawler.PENNY.brawlStarsName(),
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
                                                                Brawler.DOUG.brawlStarsId(),
                                                                Brawler.DOUG.brawlStarsName(),
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
                                                                Brawler.POCO.brawlStarsId(),
                                                                Brawler.POCO.brawlStarsName(),
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
                                                                Brawler.BULL.brawlStarsId(),
                                                                Brawler.BULL.brawlStarsName(),
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
                                                                Brawler.WILLOW.brawlStarsId(),
                                                                Brawler.WILLOW.brawlStarsName(),
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
                                                                Brawler.GROM.brawlStarsId(),
                                                                Brawler.GROM.brawlStarsName(),
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
                                                                Brawler.EIGHT_BIT.brawlStarsId(),
                                                                Brawler.EIGHT_BIT.brawlStarsName(),
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
                                                                Brawler.ROSA.brawlStarsId(),
                                                                Brawler.ROSA.brawlStarsName(),
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

        var battlePlayerEntities = battleEntity.getTeams();
        assertBattleTeams(battlePlayerEntities, battleResponse.battle().teams());
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

    private void assertBattleTeams(
            List<List<BattleCollectionEntityTeamPlayer>> actuals,
            List<List<BattleResultPlayerResponse>> expecteds) {
        assertThat(actuals).hasSize(expecteds.size());
        for (int teamIdx = 0; teamIdx < expecteds.size(); teamIdx++) {
            int expectedPlayerSize = 0;
            for (int playerIdx = 0; playerIdx < expecteds.get(teamIdx).size(); playerIdx++) {
                BattleResultPlayerResponse playerResponse = expecteds.get(teamIdx).get(playerIdx);
                if (playerResponse.brawler() != null) {
                    assertBattlePlayerWithABrawler(
                            actuals.get(teamIdx).get(playerIdx),
                            playerResponse
                    );
                    expectedPlayerSize += 1;
                } else {
                    for (int playerBrawlerIdx = 0; playerBrawlerIdx < playerResponse.brawlers().size(); playerBrawlerIdx++) {
                        assertBattlePlayerWithBrawlers(
                                actuals.get(teamIdx).get(playerIdx + playerBrawlerIdx),
                                playerResponse,
                                playerBrawlerIdx
                        );
                    }
                    expectedPlayerSize += playerResponse.brawlers().size();
                }
            }
            assertThat(actuals.get(teamIdx)).hasSize(expectedPlayerSize);
        }
    }

    private void assertBattlePlayers(
            List<List<BattleCollectionEntityTeamPlayer>> actuals,
            List<BattleResultPlayerResponse> expecteds
    ) {
        assertThat(actuals).hasSize(expecteds.size());
        for (int idx = 0; idx < expecteds.size(); idx++) {
            BattleResultPlayerResponse playerResponse = expecteds.get(idx);
            int actualBrawlerSize = 0;
            if (playerResponse.brawler() != null) {
                assertBattlePlayerWithABrawler(actuals.get(idx).get(0), playerResponse);
                actualBrawlerSize += 1;
            } else {
                for (int playerBrawlerIdx = 0; playerBrawlerIdx < playerResponse.brawlers().size(); playerBrawlerIdx++) {
                    assertBattlePlayerWithBrawlers(actuals.get(idx).get(playerBrawlerIdx), playerResponse, playerBrawlerIdx);
                    actualBrawlerSize += 1;
                }
            }
            assertThat(actuals.get(idx)).hasSize(actualBrawlerSize);
        }
    }

    private void assertBattlePlayerWithABrawler(
            BattleCollectionEntityTeamPlayer actual,
            BattleResultPlayerResponse expected
    ) {
        assertThat(actual.getBrawlStarsTag()).isEqualTo(expected.tag());
        assertThat(actual.getName()).isEqualTo(expected.name());
        assertThat(actual.getBrawler().getBrawlStarsId()).isEqualTo(expected.brawler().id());
        assertThat(actual.getBrawler().getName()).isEqualTo(expected.brawler().name());
        assertThat(actual.getBrawler().getPower()).isEqualTo(expected.brawler().power());
        assertThat(actual.getBrawler().getTrophies()).isEqualTo(expected.brawler().trophies());
        assertThat(actual.getBrawler().getTrophyChange()).isEqualTo(expected.brawler().trophyChange());
    }

    private void assertBattlePlayerWithBrawlers(
            BattleCollectionEntityTeamPlayer actual,
            BattleResultPlayerResponse expected,
            int playerBrawlerIdx
    ) {
        assertThat(actual.getBrawlStarsTag()).isEqualTo(expected.tag());
        assertThat(actual.getName()).isEqualTo(expected.name());
        assertThat(actual.getBrawler().getBrawlStarsId()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).id());
        assertThat(actual.getBrawler().getName()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).name());
        assertThat(actual.getBrawler().getPower()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).power());
        assertThat(actual.getBrawler().getTrophies()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).trophies());
        assertThat(actual.getBrawler().getTrophyChange()).isEqualTo(expected.brawlers().get(playerBrawlerIdx).trophyChange());
    }
}