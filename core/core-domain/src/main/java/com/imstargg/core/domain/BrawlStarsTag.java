package com.imstargg.core.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record BrawlStarsTag(String value) {

    private static final Pattern PATTERN = Pattern.compile("#[A-Z0-9]+");

    public BrawlStarsTag {
        Objects.requireNonNull(value);
    }

    public boolean isValid() {
        return PATTERN.matcher(value).matches();
    }
}
