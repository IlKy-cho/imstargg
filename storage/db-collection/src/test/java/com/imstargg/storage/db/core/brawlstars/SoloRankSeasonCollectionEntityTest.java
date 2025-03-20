package com.imstargg.storage.db.core.brawlstars;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SoloRankSeasonCollectionEntityTest {

    private final Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));

    @Test
    void 첫_시즌을_생성한다() {
        // given
        // when
        var result = SoloRankSeasonCollectionEntity.createFirst();

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getStartAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 2, 25, 18, 0), clock.getZone()).toOffsetDateTime()
        );
        assertThat(result.getEndAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 4, 16, 18, 0), clock.getZone()).toOffsetDateTime()
        );
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getMonth()).isEqualTo(1);
    }


    @Test
    void 다음_시즌을_생성한다() {
        // given
        var first = SoloRankSeasonCollectionEntity.createFirst();

        // when
        var season_1_2 = first.next();
        var season_1_3 = season_1_2.next();
        var season_1_4 = season_1_3.next();

        // then
        assertThat(season_1_2.getNumber()).isEqualTo(1);
        assertThat(season_1_2.getMonth()).isEqualTo(2);
        assertThat(season_1_2.getStartAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 4, 17, 18, 0), clock.getZone()).toOffsetDateTime()
        );
        assertThat(season_1_2.getEndAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 5, 14, 18, 0), clock.getZone()).toOffsetDateTime()
        );

        assertThat(season_1_3.getNumber()).isEqualTo(1);
        assertThat(season_1_3.getMonth()).isEqualTo(3);
        assertThat(season_1_3.getStartAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 5, 15, 18, 0), clock.getZone()).toOffsetDateTime()
        );
        assertThat(season_1_3.getEndAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 6, 18, 18, 0), clock.getZone()).toOffsetDateTime()
        );

        assertThat(season_1_4.getNumber()).isEqualTo(1);
        assertThat(season_1_4.getMonth()).isEqualTo(4);
        assertThat(season_1_4.getStartAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 6, 19, 18, 0), clock.getZone()).toOffsetDateTime()
        );
        assertThat(season_1_4.getEndAt()).isEqualTo(
                ZonedDateTime.of(LocalDateTime.of(2025, 7, 16, 18, 0), clock.getZone()).toOffsetDateTime()
        );
    }
}