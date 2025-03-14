package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerRenewalStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerRenewalTest {

    @Test
    void 플레이어가_갱신_가능한지_확인한다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Player player = new PlayerFixture()
                .updatedAt(OffsetDateTime.now(clock).minusSeconds(121))
                .build();

        // when
        var playerRenewal = new PlayerRenewal(player.tag(), PlayerRenewalStatus.NEW, OffsetDateTime.now(clock));

        // then
        assertThat(playerRenewal.available(player, clock)).isTrue();
    }

    @Test
    void 플레이어의_다음_업데이트_가능_시간까지_업데이트_불가능하다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Player player = new PlayerFixture()
                .updatedAt(OffsetDateTime.now(clock).minusSeconds(119))
                .build();

        // when
        var playerRenewal = new PlayerRenewal(player.tag(), PlayerRenewalStatus.NEW, OffsetDateTime.now(clock));

        // then
        assertThat(playerRenewal.available(player, clock)).isFalse();
    }

    @Test
    void 플레이어_갱신이_진행중인경우_갱신할수_없다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Player player = new PlayerFixture()
                .updatedAt(OffsetDateTime.now(clock).minusSeconds(121))
                .build();

        // when
        var executingPlayerRenewal = new PlayerRenewal(player.tag(), PlayerRenewalStatus.EXECUTING, OffsetDateTime.now(clock));
        var pendingPlayerRenewal = new PlayerRenewal(player.tag(), PlayerRenewalStatus.PENDING, OffsetDateTime.now(clock));

        // then
        assertThat(executingPlayerRenewal.available(player, clock)).isFalse();
        assertThat(pendingPlayerRenewal.available(player, clock)).isFalse();
    }

    @Test
    void 갱신중_상태이더라도_타임아웃이_초과되면_갱신_실패로_보고_다시시도_할_수_있다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Player player = new PlayerFixture()
                .updatedAt(OffsetDateTime.now(clock).minusSeconds(121))
                .build();

        // when
        var executingPlayerRenewal = new PlayerRenewal(
                player.tag(),
                PlayerRenewalStatus.EXECUTING,
                OffsetDateTime.now(clock).minusSeconds(121)
        );
        var pendingPlayerRenewal = new PlayerRenewal(
                player.tag(),
                PlayerRenewalStatus.PENDING,
                OffsetDateTime.now(clock).minusSeconds(121)
        );

        // then
        assertThat(executingPlayerRenewal.available(player, clock)).isTrue();
        assertThat(pendingPlayerRenewal.available(player, clock)).isTrue();
    }

    @Test
    void 모르는_플레이어가_갱신_가능한지_확인한다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayer unknownPlayer = new UnknownPlayer(
                new BrawlStarsTag("tag"),
                0,
                OffsetDateTime.now(clock).minusSeconds(121)
        );

        // when
        var playerRenewal = new PlayerRenewal(
                new BrawlStarsTag("tag"),
                PlayerRenewalStatus.NEW,
                OffsetDateTime.now(clock)
        );

        // then
        assertThat(playerRenewal.available(unknownPlayer, clock)).isTrue();
    }

    @Test
    void 모르는_플레이어는_notfound_횟수에_따라_다음_업데이트_가능_시간이_증가된다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        BrawlStarsTag tag = new BrawlStarsTag("tag");

        // when
        var playerRenewal = new PlayerRenewal(
                tag,
                PlayerRenewalStatus.NEW,
                OffsetDateTime.now(clock)
        );

        // then
        assertThat(playerRenewal.available(
                new UnknownPlayer(
                        tag, 0,
                        OffsetDateTime.now(clock).minusSeconds(1)
                ),
                clock
        )).isTrue();
        assertThat(playerRenewal.available(
                new UnknownPlayer(
                        tag, 1,
                        OffsetDateTime.now(clock).minusSeconds(1)
                ),
                clock
        )).isFalse();
        assertThat(playerRenewal.available(
                new UnknownPlayer(
                        tag, 1,
                        OffsetDateTime.now(clock).minusSeconds(121)
                ),
                clock
        )).isTrue();
        assertThat(playerRenewal.available(
                new UnknownPlayer(
                        tag, 2,
                        OffsetDateTime.now(clock).minusSeconds(121)
                ),
                clock
        )).isFalse();
        assertThat(playerRenewal.available(
                new UnknownPlayer(
                        tag, 2,
                        OffsetDateTime.now(clock).minusSeconds(241)
                ),
                clock
        )).isTrue();
    }

    @Test
    void 모르는_플레이어의_갱신이_진행중일_경우_갱신할_수_없다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayer unknownPlayer = new UnknownPlayer(
                new BrawlStarsTag("tag"),
                0,
                OffsetDateTime.now(clock).minusSeconds(121)
        );

        // when
        var executingPlayerRenewal = new PlayerRenewal(
                unknownPlayer.tag(),
                PlayerRenewalStatus.EXECUTING,
                OffsetDateTime.now(clock)
        );
        var pendingPlayerRenewal = new PlayerRenewal(
                unknownPlayer.tag(),
                PlayerRenewalStatus.PENDING,
                OffsetDateTime.now(clock)
        );

        // then
        assertThat(executingPlayerRenewal.available(unknownPlayer, clock)).isFalse();
        assertThat(pendingPlayerRenewal.available(unknownPlayer, clock)).isFalse();
    }

    @Test
    void 모르는_플레이어의_갱신이_진행중일_경우_타임아웃이_초과되면_갱신_실패로_보고_다시시도_할_수_있다() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayer unknownPlayer = new UnknownPlayer(
                new BrawlStarsTag("tag"),
                0,
                OffsetDateTime.now(clock).minusSeconds(121)
        );

        // when
        var executingPlayerRenewal = new PlayerRenewal(
                unknownPlayer.tag(),
                PlayerRenewalStatus.EXECUTING,
                OffsetDateTime.now(clock).minusSeconds(121)
        );
        var pendingPlayerRenewal = new PlayerRenewal(
                unknownPlayer.tag(),
                PlayerRenewalStatus.PENDING,
                OffsetDateTime.now(clock).minusSeconds(121)
        );

        // then
        assertThat(executingPlayerRenewal.available(unknownPlayer, clock)).isTrue();
        assertThat(pendingPlayerRenewal.available(unknownPlayer, clock)).isTrue();
    }

}