package com.imstargg.admin.controller.response;

import java.util.List;

public record ListResponse<T>(
        List<T> content
) {
}
