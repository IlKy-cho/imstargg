package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.PlayerStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerCollectionEntityTest {

    private final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Test
    void 최근_전투_시간을_업데이트한다() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        var latestBattleTime = OffsetDateTime.now(clock);

        var oldBattle = new BattleCollectionEntityFixture()
                .player(player)
                .battleTime(latestBattleTime.minusDays(1))
                .build();
        var latestBattle = new BattleCollectionEntityFixture()
                .player(player)
                .battleTime(latestBattleTime)
                .build();

        // when
        player.playerUpdated(clock, List.of(oldBattle, latestBattle));

        // then
        assertThat(player.getLatestBattleTime()).isEqualTo(latestBattleTime);
    }

    @Test
    void 마지막_전투로부터_30일이_지나면_휴면_상태로_변경된다() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        var oldBattleTime = OffsetDateTime.now(clock).minusDays(31);

        var battle = new BattleCollectionEntityFixture()
                .battleTime(oldBattleTime)
                .build();

        // when
        player.playerUpdated(clock, List.of(battle));

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.DORMANT);
    }

    @Test
    void 마지막_전투로부터_30일이_지나지_않으면_업데이트_상태로_변경된다() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        var recentBattleTime = OffsetDateTime.now(clock).minusDays(29);

        var battle = new BattleCollectionEntityFixture()
                .battleTime(recentBattleTime)
                .player(player)
                .build();

        // when
        player.playerUpdated(clock, List.of(battle));

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.PLAYER_UPDATED);
    }

    @Test
    void 솔로_랭크_전투의_최신_트로피를_티어로_업데이트한다() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        var latestBattleTime = OffsetDateTime.now(clock);

        var oldBattle = new BattleCollectionEntityFixture()
                .type(BattleType.SOLO_RANKED)
                .battleTime(latestBattleTime.minusHours(1))
                .player(player)
                .trophies(8)
                .build();

        var latestBattle = new BattleCollectionEntityFixture()
                .type(BattleType.SOLO_RANKED)
                .battleTime(latestBattleTime)
                .player(player)
                .trophies(7)
                .build();

        // when
        player.playerUpdated(clock, List.of(oldBattle, latestBattle));

        // then
        assertThat(player.getSoloRankTier()).isEqualTo(7);
    }

    @Test
    void 삭제된_플레이어는_업데이트되지_않는다() {
        // given
        var player = new PlayerCollectionEntityFixture().build();
        player.deleted();

        var battle = new BattleCollectionEntityFixture()
                .battleTime(OffsetDateTime.now(clock))
                .build();

        // when
        player.playerUpdated(clock, List.of(battle));

        // then
        assertThat(player.getStatus()).isEqualTo(PlayerStatus.DELETED);
        assertThat(player.getLatestBattleTime()).isNull();
    }
}