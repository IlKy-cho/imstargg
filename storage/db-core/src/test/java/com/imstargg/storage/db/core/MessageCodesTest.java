package com.imstargg.storage.db.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCodesTest {

    @Test
    void 맵_이름() {
        // given
        String mapName = "map_name";

        // when & then
        assertThat(MessageCodes.BATTLE_MAP_NAME.code(mapName))
                .isEqualTo("maps.map_name.name");
    }

    @Test
    void 브롤러_이름() {
        // given
        long brawlStarsId = 12345L;

        // when & then
        assertThat(MessageCodes.BRAWLER_NAME.code(brawlStarsId))
                .isEqualTo("brawler.12345.name");
    }

    @Test
    void 기어_이름() {
        // given
        long brawlStarsId = 12345L;

        // when & then
        assertThat(MessageCodes.GEAR_NAME.code(brawlStarsId))
                .isEqualTo("gear.12345.name");
    }

    @Test
    void 스타파워_이름() {
        // given
        long brawlStarsId = 12345L;

        // when & then
        assertThat(MessageCodes.STAR_POWER_NAME.code(brawlStarsId))
                .isEqualTo("starpower.12345.name");
    }

    @Test
    void 가젯_이름() {
        // given
        long brawlStarsId = 12345L;

        // when & then
        assertThat(MessageCodes.GADGET_NAME.code(brawlStarsId))
                .isEqualTo("gadget.12345.name");
    }
}