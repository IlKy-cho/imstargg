package com.imstargg.storage.db.core.brawlstars;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SoloRankSeasonCollectionEntityTest {

    @Test
    void 첫_시즌을_생성한다() {
        // given
        // when
        var result = SoloRankSeasonCollectionEntity.createFirst();

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getStartAt()).isEqualTo(
                OffsetDateTime.parse("2025-02-25T18:00:00+09:00")
        );
        assertThat(result.getEndAt()).isEqualTo(
                OffsetDateTime.parse("2025-04-16T18:00:00+09:00")
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
                OffsetDateTime.parse("2025-04-17T18:00:00+09:00")
        );
        assertThat(season_1_2.getEndAt()).isEqualTo(
                OffsetDateTime.parse("2025-05-14T18:00:00+09:00")
        );

        assertThat(season_1_3.getNumber()).isEqualTo(1);
        assertThat(season_1_3.getMonth()).isEqualTo(3);
        assertThat(season_1_3.getStartAt()).isEqualTo(
                OffsetDateTime.parse("2025-05-15T18:00:00+09:00")
        );
        assertThat(season_1_3.getEndAt()).isEqualTo(
                OffsetDateTime.parse("2025-06-18T18:00:00+09:00")
        );

        assertThat(season_1_4.getNumber()).isEqualTo(1);
        assertThat(season_1_4.getMonth()).isEqualTo(4);
        assertThat(season_1_4.getStartAt()).isEqualTo(
                OffsetDateTime.parse("2025-06-19T18:00:00+09:00")
        );
        assertThat(season_1_4.getEndAt()).isEqualTo(
                OffsetDateTime.parse("2025-07-16T18:00:00+09:00")
        );
    }
}