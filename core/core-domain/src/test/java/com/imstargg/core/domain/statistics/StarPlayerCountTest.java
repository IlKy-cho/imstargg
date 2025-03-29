package com.imstargg.core.domain.statistics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class StarPlayerCountTest {

    @Test
    void 전체_전투_횟수가_0일_때_starPlayerRate는_0을_반환한다() {
        // given
        StarPlayerCount starPlayerCount = new StarPlayerCount(5);
        ResultCount resultCount = new ResultCount(0, 0, 0);

        // when
        double starPlayerRate = starPlayerCount.starPlayerRate(resultCount);

        // then
        assertThat(starPlayerRate).isCloseTo(0.0, within(0.0001));
    }

    @Test
    void starPlayerRate는_스타플레이어_횟수를_전체_전투_횟수로_나눈_값을_반환한다() {
        // given
        StarPlayerCount starPlayerCount = new StarPlayerCount(5);
        ResultCount resultCount = new ResultCount(10, 5, 3);

        // when
        double starPlayerRate = starPlayerCount.starPlayerRate(resultCount);

        // then
        assertThat(starPlayerRate).isCloseTo(0.2777777777777778, within(0.0001));
    }

    @Test
    void merge는_두_StarPlayerCount의_값을_합친_새로운_StarPlayerCount를_반환한다() {
        // given
        StarPlayerCount starPlayerCount1 = new StarPlayerCount(5);
        StarPlayerCount starPlayerCount2 = new StarPlayerCount(10);

        // when
        StarPlayerCount merged = starPlayerCount1.merge(starPlayerCount2);

        // then
        assertThat(merged.count()).isEqualTo(15);
    }
}