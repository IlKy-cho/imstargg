package com.imstargg.collection.domain;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BrawlerIdHashBuilder {

    private final List<Long> brawlerIds = new ArrayList<>();

    public BrawlerIdHashBuilder addBrawlerId(long brawlerId) {
        brawlerIds.add(brawlerId);
        return this;
    }

    public BrawlerIdHash build() {
        ByteBuffer buffer = ByteBuffer.allocate(brawlerIds.size() * Long.BYTES);
        brawlerIds.stream().distinct().sorted().forEach(buffer::putLong);
        return new BrawlerIdHash(brawlerIds, buffer.array());
    }
}
