package com.imstargg.core.domain;

import java.util.regex.Pattern;

public record BrawlStarsTag(String value) {

    private static final Pattern PATTERN = Pattern.compile("#[A-Z0-9]+");

    public static boolean isValid(String value) {
        return PATTERN.matcher(value).matches();
    }

    public BrawlStarsTag {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid BrawlStarsTag: " + value);
        }
    }
}
