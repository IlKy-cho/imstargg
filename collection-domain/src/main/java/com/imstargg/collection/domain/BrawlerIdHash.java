package com.imstargg.collection.domain;

import java.util.List;

public record BrawlerIdHash(
        List<Long> brawlerIds,
        String value
) {

    public int num() {
        return brawlerIds.size();
    }
}
