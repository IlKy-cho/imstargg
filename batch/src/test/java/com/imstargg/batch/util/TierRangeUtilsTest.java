package com.imstargg.batch.util;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TierRangeUtilsTest {

    @Test
    void 문자열을_통해_TrophyRange를_찾는다() {
        // given
        // when
        // then
        assertThat(TierRangeUtils.findTrophyRange(TrophyRange.TROPHY_500_PLUS.name()))
                .isPresent()
                .get()
                .isEqualTo(TrophyRange.TROPHY_500_PLUS);
        assertThat(TierRangeUtils.findTrophyRange("NOT_EXISTS"))
                .isEmpty();
    }

    @Test
    void 문자열을_통해_SoloRankTierRange를_찾는다() {
        // given
        // when
        // then
        assertThat(TierRangeUtils.findSoloRankTierRange(SoloRankTierRange.LEGENDARY_PLUS.name()))
                .isPresent()
                .get()
                .isEqualTo(SoloRankTierRange.LEGENDARY_PLUS);
        assertThat(TierRangeUtils.findSoloRankTierRange("NOT_EXISTS"))
                .isEmpty();
    }
}