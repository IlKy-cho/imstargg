package com.imstargg.core.domain.statistics;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RankCounterTest {

    @Test
    void add_새로운_순위_데이터를_추가한다() {
        // given
        RankCounter counter = new RankCounter();
        Map<Integer, Long> rankToCount = Map.of(
                1, 10L,
                2, 20L,
                3, 30L
        );

        // when
        counter.add(rankToCount);

        // then
        assertThat(counter.getRankToCount())
                .containsExactlyInAnyOrderEntriesOf(rankToCount);
    }

    @Test
    void add_기존_순위_데이터에_누적하여_추가한다() {
        // given
        RankCounter counter = new RankCounter();
        
        // when
        counter.add(Map.of(1, 10L, 2, 20L));
        counter.add(Map.of(2, 30L, 3, 40L));

        // then
        assertThat(counter.getRankToCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        1, 10L,
                        2, 50L,  // 20 + 30
                        3, 40L
                ));
    }

    @Test
    void getRankToCount_빈_카운터의_경우_빈_맵을_반환한다() {
        // given
        RankCounter counter = new RankCounter();

        // when
        Map<Integer, Long> result = counter.getRankToCount();

        // then
        assertThat(result).isEmpty();
    }
}