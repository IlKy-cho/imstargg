package com.imstargg.core.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrophyRangeTest {
    
    @Test
    void 해당하는_범위를_모두_찾는다() {
        // given
        // when
        // then
        assertThat(TrophyRange.findAll(0)).containsExactlyInAnyOrder(
                TrophyRange.TROPHY_0_PLUS
        );
        assertThat(TrophyRange.findAll(500)).containsExactlyInAnyOrder(
                TrophyRange.TROPHY_0_PLUS,
                TrophyRange.TROPHY_500_PLUS
        );
        assertThat(TrophyRange.findAll(1000)).containsExactlyInAnyOrder(
                TrophyRange.TROPHY_0_PLUS,
                TrophyRange.TROPHY_500_PLUS,
                TrophyRange.TROPHY_1000_PLUS
        );
    }
}