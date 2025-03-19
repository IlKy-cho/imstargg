package com.imstargg.worker.domain;

import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.AccessoryResponse;
import com.imstargg.client.brawlstars.response.BattleResponse;
import com.imstargg.client.brawlstars.response.BattleResultBrawlerResponse;
import com.imstargg.client.brawlstars.response.BattleResultPlayerResponse;
import com.imstargg.client.brawlstars.response.BattleResultResponse;
import com.imstargg.client.brawlstars.response.BrawlerStatResponse;
import com.imstargg.client.brawlstars.response.EventResponse;
import com.imstargg.client.brawlstars.response.GearStatResponse;
import com.imstargg.client.brawlstars.response.ListResponse;
import com.imstargg.client.brawlstars.response.PlayerClubResponse;
import com.imstargg.client.brawlstars.response.PlayerIconResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.client.brawlstars.response.StarPowerResponse;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.PlayerBrawlerJpaRepository;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerJpaRepository;
import com.imstargg.storage.db.core.PlayerRenewalEntity;
import com.imstargg.storage.db.core.PlayerRenewalJpaRepository;
import com.imstargg.storage.db.core.test.CleanUp;
import com.imstargg.storage.db.core.test.EntityAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;

@Disabled
@Tag("context")
@SpringBootTest
@Import(EntityAppender.class)
class PlayerRenewalServiceContextTest {

    @Autowired
    private CleanUp cleanUp;

    @Autowired
    private EntityAppender entityAppender;

    @Autowired
    private Clock clock;

    @Autowired
    private PlayerRenewalService playerRenewalService;

    @MockBean
    private BrawlStarsClient brawlStarsClient;

    @Autowired
    private PlayerRenewalJpaRepository playerRenewalJpaRepository;

    @Autowired
    private PlayerJpaRepository playerJpaRepository;

    @Autowired
    private PlayerBrawlerJpaRepository playerBrawlerJpaRepository;

    @Autowired
    private BattleJpaRepository battleJpaRepository;


    @BeforeEach
    void setUp() {
        given(brawlStarsClient.getPlayerInformation("#MY"))
                .willReturn(new PlayerResponse(
                        "#MY",
                        "my-name",
                        "my-name-color",
                        new PlayerIconResponse(100),
                        20000,
                        22000,
                        127,
                        84482,
                        false,
                        5331,
                        155,
                        100,
                        8,
                        0,
                        new PlayerClubResponse("#MYCLUB", "my-club-name"),
                        List.of(
                                new BrawlerStatResponse(
                                        16000000,
                                        "brawler-1",
                                        11,
                                        26,
                                        505,
                                        513,
                                        List.of(
                                                new GearStatResponse(62000000, "gear-1", 3),
                                                new GearStatResponse(62000001, "gear-2", 3)
                                        ),
                                        List.of(
                                                new AccessoryResponse(23000000, "gadget-1"),
                                                new AccessoryResponse(23000001, "gadget-2")
                                        ),
                                        List.of(
                                                new StarPowerResponse(24000000, "star-power-1"),
                                                new StarPowerResponse(24000001, "star-power-2")
                                        )
                                ),
                                new BrawlerStatResponse(
                                        16000001,
                                        "brawler-2",
                                        7,
                                        15,
                                        289,
                                        289,
                                        List.of(),
                                        List.of(),
                                        List.of()
                                )
                        )
                ));

        given(brawlStarsClient.getPlayerRecentBattles("#MY"))
                .willReturn(new ListResponse<>(List.of(
                        new BattleResponse(
                                OffsetDateTime.now(clock),
                                new EventResponse(
                                        15000000,
                                        BattleEventMode.BRAWL_BALL.getCode(),
                                        "event-1"
                                ),
                                new BattleResultResponse(
                                        BattleMode.BRAWL_BALL.getCode(),
                                        BattleType.RANKED.getCode(),
                                        BattleResult.VICTORY.getCode(),
                                        120,
                                        null,
                                        8,
                                        new BattleResultPlayerResponse(
                                                "#MY",
                                                "my-name",
                                                new BattleResultBrawlerResponse(
                                                        16000000,
                                                        "brawler-1",
                                                        11,
                                                        505,
                                                        null
                                                ),
                                                null
                                        ),
                                        List.of(
                                                List.of(
                                                        new BattleResultPlayerResponse(
                                                                "#MY",
                                                                "my-name",
                                                                new BattleResultBrawlerResponse(
                                                                        16000000,
                                                                        "brawler-1",
                                                                        11,
                                                                        505,
                                                                        null
                                                                ),
                                                                null
                                                        ),
                                                        new BattleResultPlayerResponse(
                                                                "#TEAM1",
                                                                "team-1-name",
                                                                new BattleResultBrawlerResponse(
                                                                        16000001,
                                                                        "brawler-2",
                                                                        7,
                                                                        289,
                                                                        null
                                                                ),
                                                                null
                                                        ),
                                                        new BattleResultPlayerResponse(
                                                                "#TEAM2",
                                                                "team-2-name",
                                                                new BattleResultBrawlerResponse(
                                                                        16000002,
                                                                        "brawler-3",
                                                                        15,
                                                                        289,
                                                                        null
                                                                ),
                                                                null
                                                        )
                                                ),
                                                List.of(
                                                        new BattleResultPlayerResponse(
                                                                "#ENEMY1",
                                                                "enemy-1-name",
                                                                new BattleResultBrawlerResponse(
                                                                        16000003,
                                                                        "brawler-4",
                                                                        10,
                                                                        100,
                                                                        null
                                                                ),
                                                                null
                                                        ),
                                                        new BattleResultPlayerResponse(
                                                                "#ENEMY2",
                                                                "enemy-2-name",
                                                                new BattleResultBrawlerResponse(
                                                                        16000004,
                                                                        "brawler-5",
                                                                        12,
                                                                        120,
                                                                        null
                                                                ),
                                                                null
                                                        ),
                                                        new BattleResultPlayerResponse(
                                                                "#ENEMY3",
                                                                "enemy-3-name",
                                                                null,
                                                                null
                                                        )
                                                )
                                        ),
                                        null
                                )
                        )
                ), null));

        PlayerRenewalEntity playerRenewalEntity = new PlayerRenewalEntity(
                "#MY"
        );
        playerRenewalEntity.pending();
        entityAppender.append(playerRenewalEntity);
    }

    @AfterEach
    void tearDown() {
        cleanUp.all();
    }

    @Test
    void 플레이어를_갱신한다() {
        // given
        entityAppender.append(new PlayerCollectionEntity(
                "#MY",
                "new-name",
                "new-name-color",
                200,
                1,
                2,
                3,
                4,
                true,
                5,
                6,
                7,
                8,
                9,
                null
        ));

        // when
        playerRenewalService.renew("#MY");

        // then
    }
}
