package com.imstargg.storage.db.core;

import com.imstargg.core.enums.UnknownPlayerStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownPlayerCollectionEntityTest {

    @Test
    void 어드민_신규_플레이어_생성() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        UnknownPlayerCollectionEntity unknownPlayer = UnknownPlayerCollectionEntity
                .adminNew("1234567890", clock);

        // then
        assertThat(unknownPlayer.getId()).isNull();
        assertThat(unknownPlayer.getBrawlStarsTag()).isEqualTo("1234567890");
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.ADMIN_NEW);
        assertThat(unknownPlayer.getNotFoundCount()).isZero();
        assertThat(unknownPlayer.getUpdateAvailableAt()).isEqualTo(LocalDateTime.now(clock));
    }
}