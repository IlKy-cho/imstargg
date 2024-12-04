package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.PlayerSearchParam;
import jakarta.validation.constraints.NotNull;

public record PlayerSearchRequest(
        @NotNull String query
) {

    public PlayerSearchParam toParam() {
        return new PlayerSearchParam(query);
    }
}
