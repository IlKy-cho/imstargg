package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import jakarta.annotation.Nullable;

public record Gadget(
        BrawlStarsId id,
        String name,
        @Nullable String imagePath
) {
}
