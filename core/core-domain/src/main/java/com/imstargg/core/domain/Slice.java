package com.imstargg.core.domain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Slice<T>(
        List<T> content,
        boolean hasNext
) {

    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new Slice<>(getConvertedContent(converter), hasNext);
    }

    private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return this.content.stream().map(converter).collect(Collectors.toList());
    }
}
