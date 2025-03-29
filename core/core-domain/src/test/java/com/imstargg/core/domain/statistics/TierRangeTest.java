package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TierRangeTest {

    @Test
    void TrophyRange가_주어졌을_때_value는_TrophyRange의_name을_반환한다() {
        // given
        TrophyRange trophyRange = TrophyRange.TROPHY_500_PLUS;
        TierRange tierRange = new TierRange(trophyRange, null);

        // when
        String result = tierRange.value();

        // then
        assertThat(result).isEqualTo(trophyRange.name());
    }

    @Test
    void SoloRankTierRange가_주어졌을_때_value는_SoloRankTierRange의_name을_반환한다() {
        // given
        SoloRankTierRange soloRankTierRange = SoloRankTierRange.DIAMOND_PLUS;
        TierRange tierRange = new TierRange(null, soloRankTierRange);

        // when
        String result = tierRange.value();

        // then
        assertThat(result).isEqualTo(soloRankTierRange.name());
    }

    @Test
    void 두_값이_모두_null일_때_value는_null을_반환한다() {
        // given
        TierRange tierRange = new TierRange(null, null);

        // when
        String result = tierRange.value();

        // then
        assertThat(result).isNull();
    }
}