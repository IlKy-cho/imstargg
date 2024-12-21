package com.imstargg.core.domain;

import java.util.List;

public record Slice<T>(
        List<T> content,
        boolean hasNext
) {
}
