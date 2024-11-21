package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

public record CursorResponse(
        @Nullable String after,
        @Nullable String before
) {
}
