package com.imstargg.core.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoloRankTierTest {

    @Test
    void 값을_티어로_변환한다() {
        assertThat(SoloRankTier.of(0))
                .describedAs("1보다 작은 경우")
                .isEqualTo(SoloRankTier.BRONZE_1);
        assertThat(SoloRankTier.of(1)).isEqualTo(SoloRankTier.BRONZE_1);
        assertThat(SoloRankTier.of(2)).isEqualTo(SoloRankTier.BRONZE_2);
        assertThat(SoloRankTier.of(3)).isEqualTo(SoloRankTier.BRONZE_3);
        assertThat(SoloRankTier.of(4)).isEqualTo(SoloRankTier.SILVER_1);
        assertThat(SoloRankTier.of(5)).isEqualTo(SoloRankTier.SILVER_2);
        assertThat(SoloRankTier.of(6)).isEqualTo(SoloRankTier.SILVER_3);
        assertThat(SoloRankTier.of(7)).isEqualTo(SoloRankTier.GOLD_1);
        assertThat(SoloRankTier.of(8)).isEqualTo(SoloRankTier.GOLD_2);
        assertThat(SoloRankTier.of(9)).isEqualTo(SoloRankTier.GOLD_3);
        assertThat(SoloRankTier.of(10)).isEqualTo(SoloRankTier.DIAMOND_1);
        assertThat(SoloRankTier.of(11)).isEqualTo(SoloRankTier.DIAMOND_2);
        assertThat(SoloRankTier.of(12)).isEqualTo(SoloRankTier.DIAMOND_3);
        assertThat(SoloRankTier.of(13)).isEqualTo(SoloRankTier.MYTHIC_1);
        assertThat(SoloRankTier.of(14)).isEqualTo(SoloRankTier.MYTHIC_2);
        assertThat(SoloRankTier.of(15)).isEqualTo(SoloRankTier.MYTHIC_3);
        assertThat(SoloRankTier.of(16)).isEqualTo(SoloRankTier.LEGENDARY_1);
        assertThat(SoloRankTier.of(17)).isEqualTo(SoloRankTier.LEGENDARY_2);
        assertThat(SoloRankTier.of(18)).isEqualTo(SoloRankTier.LEGENDARY_3);
        assertThat(SoloRankTier.of(19)).isEqualTo(SoloRankTier.MASTER_1);
        assertThat(SoloRankTier.of(20)).isEqualTo(SoloRankTier.MASTER_2);
        assertThat(SoloRankTier.of(21)).isEqualTo(SoloRankTier.MASTER_3);
        assertThat(SoloRankTier.of(22)).isEqualTo(SoloRankTier.PRO);
        assertThat(SoloRankTier.of(23))
                .describedAs("22보다 큰 경우")
                .isEqualTo(SoloRankTier.PRO);
    }
}