package com.imstargg.core.domain.statistics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ResultCountTest {

    @Test
    void totalBattleCount는_승리_패배_무승부_횟수의_합을_반환한다() {
        // given
        ResultCount resultCount = new ResultCount(10, 5, 3);

        // when
        long totalBattleCount = resultCount.totalBattleCount();

        // then
        assertThat(totalBattleCount).isEqualTo(18);
    }

    @Test
    void 승리_패배_횟수가_0일_때_winRate는_0을_반환한다() {
        // given
        ResultCount resultCount = new ResultCount(0, 0, 3);

        // when
        double winRate = resultCount.winRate();

        // then
        assertThat(winRate).isCloseTo(0.0, within(0.0001));
    }

    @Test
    void winRate는_승리_횟수를_승리_패배_횟수의_합으로_나눈_값을_반환한다() {
        // given
        ResultCount resultCount = new ResultCount(10, 5, 3);

        // when
        double winRate = resultCount.winRate();

        // then
        assertThat(winRate).isCloseTo(0.6666666666666666, within(0.0001));
    }

    @Test
    void 전체_전투_횟수가_0일_때_pickRate는_0을_반환한다() {
        // given
        ResultCount resultCount = new ResultCount(10, 5, 3);

        // when
        double pickRate = resultCount.pickRate(0);

        // then
        assertThat(pickRate).isCloseTo(0.0, within(0.0001));
    }

    @Test
    void pickRate는_전체_전투_횟수에서_해당_결과의_비율을_반환한다() {
        // given
        ResultCount resultCount = new ResultCount(10, 5, 3);

        // when
        double pickRate = resultCount.pickRate(100);

        // then
        assertThat(pickRate).isCloseTo(0.18, within(0.0001));
    }

    @Test
    void merge는_두_ResultCount의_값을_합친_새로운_ResultCount를_반환한다() {
        // given
        ResultCount resultCount1 = new ResultCount(10, 5, 3);
        ResultCount resultCount2 = new ResultCount(20, 10, 6);

        // when
        ResultCount merged = resultCount1.merge(resultCount2);

        // then
        assertThat(merged.victoryCount()).isEqualTo(30);
        assertThat(merged.defeatCount()).isEqualTo(15);
        assertThat(merged.drawCount()).isEqualTo(9);
    }
}