package com.imstargg.core.domain.statistics;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class RankCountTest {

    @Test
    void totalBattleCount_정상적으로_전체_배틀_수를_계산한다() {
        // given
        RankCount rankCount = new RankCount(Map.of(
                1, 10L,
                2, 20L,
                3, 30L
        ));

        // when
        long total = rankCount.totalBattleCount();

        // then
        assertThat(total).isEqualTo(60L);
    }

    @Test
    void averageRank_정상적으로_평균_순위를_계산한다() {
        // given
        RankCount rankCount = new RankCount(Map.of(
                1, 10L,  // 1 * 10 = 10
                2, 20L,  // 2 * 20 = 40
                3, 30L   // 3 * 30 = 90
        ));             // 합계 140 / 전체 수 60 = 2.333...

        // when
        double average = rankCount.averageRank();

        // then
        assertThat(average).isCloseTo(2.3333333333333335, within(0.000001));
    }

    @Test
    void pickRate_정상적으로_픽률을_계산한다() {
        // given
        RankCount rankCount = new RankCount(Map.of(
                1, 10L,
                2, 20L,
                3, 30L
        ));
        long totalBattles = 120L;

        // when
        double pickRate = rankCount.pickRate(totalBattles);

        // then
        assertThat(pickRate).isCloseTo(0.5, within(0.000001));
    }

    @Test
    void pickRate_전체_배틀_수가_0이면_0을_반환한다() {
        // given
        RankCount rankCount = new RankCount(Map.of(
                1, 10L,
                2, 20L
        ));

        // when
        double pickRate = rankCount.pickRate(0);

        // then
        assertThat(pickRate).isZero();
    }

    @Test
    void merge_두_RankCount를_정상적으로_병합한다() {
        // given
        RankCount rankCount1 = new RankCount(Map.of(
                1, 10L,
                2, 20L
        ));
        RankCount rankCount2 = new RankCount(Map.of(
                2, 30L,
                3, 40L
        ));

        // when
        RankCount merged = rankCount1.merge(rankCount2);

        // then
        assertThat(merged.rankToCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        1, 10L,
                        2, 50L,  // 20 + 30
                        3, 40L
                ));
    }
}