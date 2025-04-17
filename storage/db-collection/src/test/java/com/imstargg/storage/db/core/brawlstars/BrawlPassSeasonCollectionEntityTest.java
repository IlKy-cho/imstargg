package com.imstargg.storage.db.core.brawlstars;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BrawlPassSeasonCollectionEntityTest {

    @Test
    void _37_시즌을_생성한다() {
        // when
        var result = BrawlPassSeasonCollectionEntity.create37Season();

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getNumber()).isEqualTo(37);
        assertThat(result.getStartAt()).isEqualTo(OffsetDateTime.parse("2025-04-03T18:00:00+09:00"));
        assertThat(result.getEndAt()).isEqualTo(OffsetDateTime.parse("2025-05-01T18:00:00+09:00"));
    }

    @Test
    void 다음_시즌을_생성한다() {
        // given
        var season37 = BrawlPassSeasonCollectionEntity.create37Season();

        // when
        var season38 = season37.next();
        var season39 = season38.next();
        var season40 = season39.next();

        // then
        assertThat(season38.getNumber()).isEqualTo(38);
        assertThat(season38.getStartAt()).isEqualTo(OffsetDateTime.parse("2025-05-01T18:00:00+09:00"));
        assertThat(season38.getEndAt()).isEqualTo(OffsetDateTime.parse("2025-06-05T18:00:00+09:00"));

        assertThat(season39.getNumber()).isEqualTo(39);
        assertThat(season39.getStartAt()).isEqualTo(OffsetDateTime.parse("2025-06-05T18:00:00+09:00"));
        assertThat(season39.getEndAt()).isEqualTo(OffsetDateTime.parse("2025-07-03T18:00:00+09:00"));

        assertThat(season40.getNumber()).isEqualTo(40);
        assertThat(season40.getStartAt()).isEqualTo(OffsetDateTime.parse("2025-07-03T18:00:00+09:00"));
        assertThat(season40.getEndAt()).isEqualTo(OffsetDateTime.parse("2025-08-07T18:00:00+09:00"));
    }
}