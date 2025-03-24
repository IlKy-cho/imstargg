package com.imstargg.core.enums;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoloRankTierRangeTest {

    @Test
    void 해당하는_범위를_모두_찾는다() {
        // given
        // when
        // then
        assertThat(SoloRankTierRange.findAll(SoloRankTier.BRONZE_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.BRONZE_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.BRONZE_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.SILVER_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.SILVER_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.SILVER_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.GOLD_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.GOLD_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.GOLD_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.DIAMOND_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.DIAMOND_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.DIAMOND_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MYTHIC_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MYTHIC_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MYTHIC_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.LEGENDARY_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.LEGENDARY_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.LEGENDARY_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MASTER_1.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS,
                SoloRankTierRange.MASTER_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MASTER_2.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS,
                SoloRankTierRange.MASTER_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.MASTER_3.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS,
                SoloRankTierRange.MASTER_PLUS
        );
        assertThat(SoloRankTierRange.findAll(SoloRankTier.PRO.getValue())).containsExactlyInAnyOrder(
                SoloRankTierRange.BRONZE_SILVER_GOLD_PLUS,
                SoloRankTierRange.DIAMOND_PLUS,
                SoloRankTierRange.MYTHIC_PLUS,
                SoloRankTierRange.LEGENDARY_PLUS,
                SoloRankTierRange.MASTER_PLUS
        );
    }
}