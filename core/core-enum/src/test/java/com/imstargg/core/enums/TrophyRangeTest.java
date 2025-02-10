package com.imstargg.core.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrophyRangeTest {
    
    @Test
    void 트로피가_0에서_500사이일때() {
        // given
        int trophy = 300;

        // when
        TrophyRange result = TrophyRange.of(trophy);

        // then
        assertThat(result).isEqualTo(TrophyRange.TROPHY_0_500);
    }

    @Test
    void 트로피가_501에서_1000사이일때() {
        // given
        int trophy = 750;

        // when
        TrophyRange result = TrophyRange.of(trophy);

        // then
        assertThat(result).isEqualTo(TrophyRange.TROPHY_501_1000);
    }

    @Test
    void 트로피가_1000초과일때() {
        // given
        int trophy = 1500;

        // when
        TrophyRange result = TrophyRange.of(trophy);

        // then
        assertThat(result).isEqualTo(TrophyRange.TROPHY_1000_OVER);
    }

    @Test
    void 랭크전일때_트로피범위_반환() {
        // given
        BattleType battleType = BattleType.RANKED;
        int trophy = 800;

        // when
        TrophyRange result = TrophyRange.of(battleType, trophy);

        // then
        assertThat(result).isEqualTo(TrophyRange.TROPHY_501_1000);
    }

    @Test
    void 랭크전이_아닐때_null_반환() {
        // given
        BattleType battleType = BattleType.SOLO_RANKED;
        int trophy = 800;

        // when
        TrophyRange result = TrophyRange.of(battleType, trophy);

        // then
        assertThat(result).isNull();
    }
}