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

    @Test
    void 업데이트_신규_플레이어_생성() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // when
        UnknownPlayerCollectionEntity unknownPlayer = UnknownPlayerCollectionEntity
                .updateNew("1234567890", clock);

        // then
        assertThat(unknownPlayer.getId()).isNull();
        assertThat(unknownPlayer.getBrawlStarsTag()).isEqualTo("1234567890");
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.UPDATE_NEW);
        assertThat(unknownPlayer.getNotFoundCount()).isZero();
        assertThat(unknownPlayer.getUpdateAvailableAt()).isEqualTo(LocalDateTime.now(clock));
    }

    @Test
    void ADMIN_NEW_상태에서_NOT_FOUND_호출시_DELETED로_변경() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayerCollectionEntity unknownPlayer = UnknownPlayerCollectionEntity
                .adminNew("1234567890", clock);

        // when
        unknownPlayer.notFound();

        // then
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.DELETED);
        assertThat(unknownPlayer.getNotFoundCount()).isZero();
    }

    @Test
    void UPDATE_NEW_상태에서_NOT_FOUND_호출시_DELETED로_변경() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayerCollectionEntity unknownPlayer = UnknownPlayerCollectionEntity
                .updateNew("1234567890", clock);

        // when
        unknownPlayer.notFound();

        // then
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.DELETED);
        assertThat(unknownPlayer.getNotFoundCount()).isZero();
    }

    @Test
    void SEARCH_NEW_상태에서_NOT_FOUND_호출시_NOT_FOUND로_변경() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayerCollectionEntity unknownPlayer = new UnknownPlayerCollectionEntity(
                "1234567890",
                UnknownPlayerStatus.SEARCH_NEW,
                LocalDateTime.now(clock)
        );

        // when
        unknownPlayer.notFound();

        // then
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.NOT_FOUND);
        assertThat(unknownPlayer.getNotFoundCount()).isEqualTo(1);
    }

    @Test
    void NOT_FOUND_상태에서_NOT_FOUND_호출시_카운트_증가() {
        // given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        UnknownPlayerCollectionEntity unknownPlayer = new UnknownPlayerCollectionEntity(
                "1234567890",
                UnknownPlayerStatus.SEARCH_NEW,
                LocalDateTime.now(clock)
        );
        unknownPlayer.notFound();

        // when
        unknownPlayer.notFound(); // 두 번째 notFound 호출

        // then
        assertThat(unknownPlayer.getStatus()).isEqualTo(UnknownPlayerStatus.NOT_FOUND);
        assertThat(unknownPlayer.getNotFoundCount()).isEqualTo(2);
    }
}