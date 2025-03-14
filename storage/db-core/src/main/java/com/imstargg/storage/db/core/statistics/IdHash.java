package com.imstargg.storage.db.core.statistics;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record IdHash(
        byte[] value
) {

    public static IdHash of(List<Long> brawlerIds) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * brawlerIds.size());
        brawlerIds.stream()
                .sorted()
                .forEach(buffer::putLong);
        return new IdHash(buffer.array());
    }

    public int num() {
        return value.length / Long.BYTES;
    }

    public List<Long> ids() {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        List<Long> ids = new ArrayList<>();
        while (buffer.hasRemaining()) {
            ids.add(buffer.getLong());
        }
        return Collections.unmodifiableList(ids);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdHash that = (IdHash) o;
        return Objects.deepEquals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return "IdHash{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}
