package com.imstargg.storage.db.core.brawlstars;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class BrawlPassSeasonCollectionEntityTest {

    @Test
    void 다음_시즌_생성_테스트() {
        OffsetDateTime startTime = OffsetDateTime.of(2023, 10, 1, 18, 0, 0, 0, ZoneOffset.ofHours(9));
        OffsetDateTime endTime = OffsetDateTime.of(2023, 11, 1, 18, 0, 0, 0, ZoneOffset.ofHours(9));
        BrawlPassSeasonCollectionEntity currentSeason = new BrawlPassSeasonCollectionEntity(1, startTime, endTime);

        BrawlPassSeasonCollectionEntity nextSeason = currentSeason.next();

        assertThat(nextSeason.getNumber()).isEqualTo(2);
        assertThat(nextSeason.getStartTime()).isEqualTo(endTime);
        assertThat(nextSeason.getEndTime()).isEqualTo(OffsetDateTime.of(2023, 12, 7, 18, 0, 0, 0, ZoneOffset.ofHours(9)));
    }
}