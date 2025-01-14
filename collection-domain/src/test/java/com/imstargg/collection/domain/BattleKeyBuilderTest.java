package com.imstargg.collection.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BattleKeyBuilderTest {

    @Test
    void 길이64의_배틀키를_생성한다() {
        // given

        // when
        var result = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                        .addPlayerTag("player1")
                        .addPlayerTag("player2")
                        .build();

        // then
        assertThat(result).hasSize(64);
    }

    @Test
    void 플레이어_태그_순서는_달라도_된다() {
        // given

        // when
        var result1 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .addPlayerTag("player2")
                .build();
        var result2 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player2")
                .addPlayerTag("player1")
                .build();

        // then
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    void 날짜가_다르면_다른_키가_생성된다() {
        // given

        // when
        var result1 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .addPlayerTag("player2")
                .build();
        var result2 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 2, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .addPlayerTag("player2")
                .build();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }

    @Test
    void 플레이어_수가_다르면_다른_키가_생성된다() {
        // given

        // when
        var result1 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .addPlayerTag("player2")
                .build();
        var result2 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .build();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }

    @Test
    void 플레이어가_다르면_다른_키가_생성된다() {
        // given

        // when
        var result1 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player1")
                .addPlayerTag("player2")
                .build();
        var result2 = new BattleKeyBuilder(ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0), ZoneId.systemDefault()).toOffsetDateTime())
                .addPlayerTag("player3")
                .addPlayerTag("player4")
                .build();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }

}