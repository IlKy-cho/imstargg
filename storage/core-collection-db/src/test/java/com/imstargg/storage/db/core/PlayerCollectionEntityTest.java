package com.imstargg.storage.db.core;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.test.AbstractDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerCollectionEntityTest extends AbstractDataJpaTest {

    @Autowired
    private PlayerCollectionJpaRepository repository;

    @Test
    void 생성하면_notUpdatedCount는_0_updateWeight는_30분후로_초기화되고_정상적으로_저장된다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        PlayerCollectionEntity entity = new PlayerCollectionEntity(
                PlayerStatus.UPDATED,
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
                LocalDateTime.now(clock)
        );

        // when
        repository.save(entity);

        // then
        PlayerCollectionEntity savedEntity = repository.findById(entity.getId()).get();
        assertThat(savedEntity.getNotUpdatedCount()).isZero();
        assertThat(savedEntity.getUpdateWeight())
                .isEqualTo(LocalDateTime.now(clock).plusMinutes(30));
        assertThat(savedEntity.getStatus()).isEqualTo(PlayerStatus.UPDATED);
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
}