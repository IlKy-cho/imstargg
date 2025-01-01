package com.imstargg.collection.domain;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record BrawlerIdHash(
        byte[] value
) {

    public static BrawlerIdHash of(List<Long> brawlerIds) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * brawlerIds.size());
        brawlerIds.stream()
                .sorted()
                .forEach(buffer::putLong);
        return new BrawlerIdHash(buffer.array());
    }

    public int num() {
        return value.length / Long.BYTES;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BrawlerIdHash that = (BrawlerIdHash) o;
        return Objects.deepEquals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "BrawlerIdHash{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}
