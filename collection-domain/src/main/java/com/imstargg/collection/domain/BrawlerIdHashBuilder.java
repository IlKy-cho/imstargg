package com.imstargg.collection.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrawlerIdHashBuilder {

    private final List<Long> brawlerIds = new ArrayList<>();

    public BrawlerIdHashBuilder addBrawlerId(long brawlerId) {
        brawlerIds.add(brawlerId);
        return this;
    }

    public BrawlerIdHash build() {
        String hashValue = brawlerIds.stream()
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return new BrawlerIdHash(brawlerIds, hashValue);
    }
}
