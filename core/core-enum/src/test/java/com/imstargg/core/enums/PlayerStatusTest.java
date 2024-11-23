package com.imstargg.core.enums;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerStatusTest {

    @Test
    void UPDATE_NEW_상태는_업데이트_가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.UPDATE_NEW.isUpdatable(clock, LocalDateTime.now()))
                .isTrue();
    }

    @Test
    void REFRESH_NEW_상태는_업데이트_가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.REFRESH_NEW.isUpdatable(clock, LocalDateTime.now()))
                .isTrue();
    }

    @Test
    void REFRESH_REQUESTED_상태는_업데이트_가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.REFRESH_REQUESTED.isUpdatable(clock, LocalDateTime.now()))
                .isTrue();
    }

    @Test
    void DELETED_상태는_업데이트_불가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.DELETED.isUpdatable(clock, LocalDateTime.now()))
                .isFalse();
    }

    @Test
    void NOT_FOUND_상태는_업데이트_된_후_2분이_지나야_업데이트_가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.NOT_FOUND.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(4)))
                .isTrue();
        assertThat(PlayerStatus.NOT_FOUND.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(3)))
                .isTrue();
        assertThat(PlayerStatus.NOT_FOUND.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(2)))
                .isFalse();
        assertThat(PlayerStatus.NOT_FOUND.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(1)))
                .isFalse();
    }

    @Test
    void UPDATED_상태는_업데이트_된_후_2분이_지나야_업데이트_가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        // then
        assertThat(PlayerStatus.UPDATED.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(4)))
                .isTrue();
        assertThat(PlayerStatus.UPDATED.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(3)))
                .isTrue();
        assertThat(PlayerStatus.UPDATED.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(2)))
                .isFalse();
        assertThat(PlayerStatus.UPDATED.isUpdatable(clock, LocalDateTime.now(clock).minusMinutes(1)))
                .isFalse();
    }
}