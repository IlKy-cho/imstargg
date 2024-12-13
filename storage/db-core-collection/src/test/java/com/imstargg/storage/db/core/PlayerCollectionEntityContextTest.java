package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
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
                "brawlStarsClubTag",
                clock
        );

        // when
        repository.save(entity);

        // then
        PlayerCollectionEntity savedEntity = repository.findById(entity.getId()).get();
        assertThat(savedEntity.getNotUpdatedCount()).isZero();
        assertThat(savedEntity.getUpdateWeight())
                .isEqualTo(LocalDateTime.now(clock));
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
                "brawlStarsClubTag",
                clock
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

    @Test
    void 새_플레이어는_전투를_업데이트하면_PLAYER_UPDATED_상태가_된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());

        List<LocalDateTime> updatedBattleTimes = List.of(
                LocalDateTime.of(2021, 1, 1, 0, 0)
        );

        // when
        player.battleUpdated(clock, updatedBattleTimes);

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.PLAYER_UPDATED);
        assertThat(player.getLatestBattleTime()).isEqualTo(LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    @Test
    void 기존_플레이어는_전투를_업데이트하면_BATTLE_UPDATED_상태가_된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());
        player.battleUpdated(clock, List.of(LocalDateTime.of(2021, 1, 1, 0, 0)));

        List<LocalDateTime> updatedBattleTimes = List.of(
                LocalDateTime.of(2021, 1, 2, 0, 0)
        );

        // when
        player.battleUpdated(clock, updatedBattleTimes);

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.BATTLE_UPDATED);
        assertThat(player.getLatestBattleTime()).isEqualTo(LocalDateTime.of(2021, 1, 2, 0, 0));
    }

    @Test
    void 전투_기록이_없으면_PLAYER_UPDATED_상태가_되고_12시간_후로_업데이트_시간이_설정된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());
        List<LocalDateTime> emptyBattles = Collections.emptyList();

        // when
        player.battleUpdated(clock, emptyBattles);

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.PLAYER_UPDATED);
        assertThat(player.getUpdateWeight()).isEqualTo(LocalDateTime.now(clock).plusHours(12));
    }

    @Test
    void 전투_기록이_30일_이상_없으면_DORMANT_상태가_된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());
        LocalDateTime oldBattleTime = LocalDateTime.now(clock).minusDays(31);
        player.battleUpdated(clock, List.of(oldBattleTime));
        List<LocalDateTime> emptyBattles = Collections.emptyList();

        // when
        player.battleUpdated(clock, emptyBattles);

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.DORMANT);
    }

    @Test
    void 최근_30분_이내_전투는_가중치가_적용된_업데이트_시간을_가진다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture()
                .trophies(10000)  // trophyWeight: 3
                .expLevel(50)     // expLevelWeight: 2
                .build());
        LocalDateTime recentBattle = LocalDateTime.now(clock).minusMinutes(20);

        // when
        player.battleUpdated(clock, List.of(recentBattle));

        // then
        long expectedWeightMultiplier = 6L; // trophyWeight(3) * expLevelWeight(2)
        assertThat(player.getUpdateWeight())
                .isEqualTo(LocalDateTime.now(clock).plusMinutes(60 * expectedWeightMultiplier));
        System.out.println(player.getUpdateWeight());
    }

    @Test
    void 전투_기록이_30분_이상_지났으면_12시간_후로_업데이트_시간이_설정된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());
        LocalDateTime oldBattle = LocalDateTime.now(clock).minusHours(1);

        // when
        player.battleUpdated(clock, List.of(oldBattle));

        // then
        assertThat(player.getUpdateWeight())
                .isEqualTo(LocalDateTime.now(clock).plusHours(12));
    }

    @Test
    void 여러_전투_기록_중_가장_최근_시간이_latestBattleTime으로_설정된다() {
        // given
        PlayerCollectionEntity player = repository.save(new PlayerCollectionEntityFixture().build());
        List<LocalDateTime> battleTimes = List.of(
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2021, 1, 3, 0, 0),
                LocalDateTime.of(2021, 1, 2, 0, 0)
        );

        // when
        player.battleUpdated(clock, battleTimes);

        // then
        assertThat(player.getLatestBattleTime())
                .isEqualTo(LocalDateTime.of(2021, 1, 3, 0, 0));
    }
}