package com.imstargg.core.api.controller.v1.request;

import jakarta.validation.constraints.Positive;

public record PageRequest(
        @Positive Integer page
) {

    public PageRequest {
        if (page == null) {
            page = 1;
        }
    }
}
