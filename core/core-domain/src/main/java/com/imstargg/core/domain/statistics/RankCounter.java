package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.Map;

public class RankCounter {

    private final Map<Integer, Long> rankToCount = new HashMap<>();

    public void add(Map<Integer, Long> rankToCounts) {
        rankToCounts.forEach((rank, count) ->
                this.rankToCount.merge(rank, count, Long::sum)
        );
    }

    public Map<Integer, Long> getRankToCount() {
        return rankToCount;
    }
}
