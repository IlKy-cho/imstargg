package com.imstargg.core.domain.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RankCounter {

    private final Map<Integer, Counter> rankToCount = new HashMap<>();

    public void add(Map<Integer, Long> rankToCount) {
        rankToCount.forEach((rank, count) ->
                this.rankToCount.computeIfAbsent(rank, k -> new Counter()).add(count)
        );
    }

    public Map<Integer, Long> getRankToCount() {
        return rankToCount.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getCount()));
    }
}
