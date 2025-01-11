package com.imstargg.core.api.controller.request;

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
