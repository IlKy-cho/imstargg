package com.imstargg.core.api.controller.response;

import java.util.List;

public record ListResponse<T>(
        List<T> content
) {
}
