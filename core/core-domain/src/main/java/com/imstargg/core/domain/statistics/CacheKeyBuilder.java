package com.imstargg.core.domain.statistics;

import java.util.LinkedHashMap;

public class CacheKeyBuilder {

    private static final String SEPARATOR = ":";

    private final LinkedHashMap<String, Object> keyValues = new LinkedHashMap<>();
    private final String prefix;
    private final String version;

    public CacheKeyBuilder(String prefix, String version) {
        this.prefix = prefix;
        this.version = version;
    }

    public CacheKeyBuilder add(String key, Object value) {
        keyValues.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(prefix);
        builder.append(SEPARATOR).append(version);
        keyValues.forEach((key, value) -> builder.append(SEPARATOR).append(key).append(SEPARATOR).append(value));
        return builder.toString();
    }
}
