package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import jakarta.annotation.Nullable;

public record Gadget(
        BrawlStarsId id,
        MessageCollection names,
        @Nullable String imagePath
) {
}
