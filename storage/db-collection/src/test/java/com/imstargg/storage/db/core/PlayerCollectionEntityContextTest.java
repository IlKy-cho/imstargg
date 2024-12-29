package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("context")
class PlayerCollectionEntityContextTest extends AbstractDataJpaTest {

    @Autowired
    private PlayerCollectionJpaRepository repository;

    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Test
    void 생성하면_notUpdatedCount는_0_updateWeight는_now로_초기화되고_정상적으로_저장된다() {
        // given
        PlayerCollectionEntity entity = new PlayerCollectionEntity(
                "brawlStarsTag",
                "name",
                "nameColor",
                1,
                1000,
                2000,
                10,
                5000,
                false,
                50,
                20,
                30,
                120,
                60,
                "brawlStarsClubTag"
        );

        // when
        repository.save(entity);

        // then
        PlayerCollectionEntity savedEntity = repository.findById(entity.getId()).get();
        assertThat(savedEntity.getStatus()).isEqualTo(PlayerStatus.NEW);
        assertThat(savedEntity.getBrawlStarsTag()).isEqualTo("brawlStarsTag");
        assertThat(savedEntity.getName()).isEqualTo("name");
        assertThat(savedEntity.getNameColor()).isEqualTo("nameColor");
        assertThat(savedEntity.getIconBrawlStarsId()).isEqualTo(1);
        assertThat(savedEntity.getTrophies()).isEqualTo(1000);
        assertThat(savedEntity.getHighestTrophies()).isEqualTo(2000);
        assertThat(savedEntity.getExpLevel()).isEqualTo(10);
        assertThat(savedEntity.getExpPoints()).isEqualTo(5000);
        assertThat(savedEntity.isQualifiedFromChampionshipChallenge()).isFalse();
        assertThat(savedEntity.getVictories3vs3()).isEqualTo(50);
        assertThat(savedEntity.getSoloVictories()).isEqualTo(20);
        assertThat(savedEntity.getDuoVictories()).isEqualTo(30);
        assertThat(savedEntity.getBestRoboRumbleTime()).isEqualTo(120);
        assertThat(savedEntity.getBestTimeAsBigBrawler()).isEqualTo(60);
        assertThat(savedEntity.getBrawlStarsClubTag()).isEqualTo("brawlStarsClubTag");
    }

    @Test
    void 브롤러를_업데이트할_수_있다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        PlayerCollectionEntity player = new PlayerCollectionEntity(
                "brawlStarsTag",
                "name",
                "nameColor",
                1,
                1000,
                2000,
                10,
                5000,
                false,
                50,
                20,
                30,
                120,
                60,
                "brawlStarsClubTag"
        );
        player.updateBrawler(
                1,
                1,
                1,
                0,
                0,
                List.of(),
                List.of(),
                List.of()
        );
        repository.save(player);

        em.flush();
        em.clear();

        // when
        PlayerCollectionEntity foundPlayer = repository.findById(player.getId()).get();
        foundPlayer.updateBrawler(
                1,
                2,
                2,
                100,
                200,
                List.of(1L, 2L),
                List.of(3L, 4L),
                List.of(5L, 6L)
        );
        foundPlayer.updateBrawler(
                2,
                3,
                3,
                300,
                400,
                List.of(7L, 8L),
                List.of(9L, 10L),
                List.of(11L, 12L)
        );
        repository.save(foundPlayer);

        em.flush();
        em.clear();

        // then
        PlayerCollectionEntity updatedPlayer = repository.findById(foundPlayer.getId()).get();
        assertThat(updatedPlayer.getBrawlers())
                .hasSize(2)
                .anySatisfy(playerBrawler -> {
                    assertThat(playerBrawler.getBrawlerBrawlStarsId()).isEqualTo(1);
                    assertThat(playerBrawler.getPower()).isEqualTo(2);
                    assertThat(playerBrawler.getRank()).isEqualTo(2);
                    assertThat(playerBrawler.getTrophies()).isEqualTo(100);
                    assertThat(playerBrawler.getHighestTrophies()).isEqualTo(200);
                    assertThat(playerBrawler.getGearBrawlStarsIds()).containsExactly(1L, 2L);
                    assertThat(playerBrawler.getStarPowerBrawlStarsIds()).containsExactly(3L, 4L);
                    assertThat(playerBrawler.getGadgetBrawlStarsIds()).containsExactly(5L, 6L);
                })
                .anySatisfy(playerBrawler -> {
                    assertThat(playerBrawler.getBrawlerBrawlStarsId()).isEqualTo(2);
                    assertThat(playerBrawler.getPower()).isEqualTo(3);
                    assertThat(playerBrawler.getRank()).isEqualTo(3);
                    assertThat(playerBrawler.getTrophies()).isEqualTo(300);
                    assertThat(playerBrawler.getHighestTrophies()).isEqualTo(400);
                    assertThat(playerBrawler.getGearBrawlStarsIds()).containsExactly(7L, 8L);
                    assertThat(playerBrawler.getStarPowerBrawlStarsIds()).containsExactly(9L, 10L);
                    assertThat(playerBrawler.getGadgetBrawlStarsIds()).containsExactly(11L, 12L);
                });
    }

}