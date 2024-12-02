package com.imstargg.batch.domain;

import com.imstargg.client.brawlstars.response.PlayerClubResponse;
import com.imstargg.client.brawlstars.response.PlayerIconResponse;
import com.imstargg.client.brawlstars.response.PlayerResponse;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerUpdateApplierTest {

    private PlayerUpdateApplier playerUpdateApplier;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.now(), Clock.systemDefaultZone().getZone());
        playerUpdateApplier = new PlayerUpdateApplier(clock);
    }

    @Test
    void 플레이어를_업데이트한다() {
        // given
        PlayerCollectionEntity playerEntity = new PlayerCollectionEntityFixture().build();

        PlayerResponse playerResponse = new PlayerResponse(
                "response-tag",
                "response-name",
                "response-name-color",
                new PlayerIconResponse(1),
                playerEntity.getTrophies() + 100,
                playerEntity.getHighestTrophies() + 100,
                playerEntity.getExpLevel() + 1,
                playerEntity.getExpPoints() + 1000,
                true,
                playerEntity.getVictories3vs3() + 1,
                playerEntity.getSoloVictories() + 1,
                playerEntity.getDuoVictories() + 1,
                playerEntity.getBestRoboRumbleTime() + 1,
                playerEntity.getBestTimeAsBigBrawler() + 1,
                new PlayerClubResponse(
                        "response-club-tag",
                        "response-club-name"
                ),
                List.of()
        );

        // when
        PlayerCollectionEntity updatedPlayerEntity = playerUpdateApplier.update(playerEntity, playerResponse);

        // then
        assertThat(updatedPlayerEntity.getName()).isEqualTo(playerResponse.name());
        assertThat(updatedPlayerEntity.getNameColor()).isEqualTo(playerResponse.nameColor());
        assertThat(updatedPlayerEntity.getIconBrawlStarsId()).isEqualTo(playerResponse.icon().id());
        assertThat(updatedPlayerEntity.getTrophies()).isEqualTo(playerResponse.trophies());
        assertThat(updatedPlayerEntity.getHighestTrophies()).isEqualTo(playerResponse.highestTrophies());
        assertThat(updatedPlayerEntity.getExpLevel()).isEqualTo(playerResponse.expLevel());
        assertThat(updatedPlayerEntity.getExpPoints()).isEqualTo(playerResponse.expPoints());
        assertThat(updatedPlayerEntity.isQualifiedFromChampionshipChallenge()).isEqualTo(playerResponse.isQualifiedFromChampionshipChallenge());
        assertThat(updatedPlayerEntity.getVictories3vs3()).isEqualTo(playerResponse.victories3vs3());
        assertThat(updatedPlayerEntity.getSoloVictories()).isEqualTo(playerResponse.soloVictories());
        assertThat(updatedPlayerEntity.getDuoVictories()).isEqualTo(playerResponse.duoVictories());
        assertThat(updatedPlayerEntity.getBestRoboRumbleTime()).isEqualTo(playerResponse.bestRoboRumbleTime());
        assertThat(updatedPlayerEntity.getBestTimeAsBigBrawler()).isEqualTo(playerResponse.bestTimeAsBigBrawler());
        assertThat(updatedPlayerEntity.getBrawlStarsClubTag()).isEqualTo(playerResponse.club().tag());
    }
}