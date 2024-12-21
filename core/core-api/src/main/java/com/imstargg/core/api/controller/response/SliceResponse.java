package com.imstargg.core.api.controller.response;

import com.imstargg.core.domain.Slice;

import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        boolean hasNext
) {

    public static <T> SliceResponse<T> of(Slice<T> slice) {
        return new SliceResponse<>(slice.content(), slice.hasNext());
    }
}
