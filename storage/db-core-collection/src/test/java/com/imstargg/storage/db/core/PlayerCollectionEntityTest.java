package com.imstargg.storage.db.core;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerCollectionEntityTest {

    @Test
    void 배틀_업데이트_후_최신_배틀_시간이_설정된다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var playerEntity = new PlayerCollectionEntityFixture().build();

        // when
        playerEntity.battleUpdated(
                clock,
                List.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0),
                        LocalDateTime.of(2021, 1, 3, 0, 0),
                        LocalDateTime.of(2021, 1, 2, 0, 0)
                )
        );

        // then
        assertThat(playerEntity.getLatestBattleTime()).isEqualTo(LocalDateTime.of(2021, 1, 3, 0, 0));
    }
}