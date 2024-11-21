package com.imstargg.core.api.controller;

import java.util.List;

public record ListResponse<T>(
        List<T> content
) {
}
