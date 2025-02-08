package com.imstargg.storage.db.core.cache;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CacheKeyBuilder {

    private static final String SEPARATOR = ":";

    private final String prefix;
    private final String version;
    private final List<String> keyValues = new ArrayList<>();

    public CacheKeyBuilder(String prefix, String version) {
        this.prefix = prefix;
        this.version = version;
    }

    public CacheKeyBuilder add(@Nullable String keyValue) {
        keyValues.add(keyValue);
        return this;
    }

    public CacheKeyBuilder add(int keyValue) {
        keyValues.add(String.valueOf(keyValue));
        return this;
    }

    public CacheKeyBuilder add(long keyValue) {
        keyValues.add(String.valueOf(keyValue));
        return this;
    }

    public CacheKeyBuilder add(boolean keyValue) {
        keyValues.add(String.valueOf(keyValue));
        return this;
    }

    public CacheKeyBuilder add(@Nullable LocalDate keyValue) {
        keyValues.add(keyValue != null ? keyValue.toString() : null);
        return this;
    }

    public CacheKeyBuilder add(@Nullable Enum<?> keyValue) {
        keyValues.add(keyValue != null ? keyValue.name() : null);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(prefix);
        builder.append(SEPARATOR).append(version);
        keyValues.forEach(keyValue -> builder.append(SEPARATOR).append(keyValue));
        return builder.toString();
    }
}
