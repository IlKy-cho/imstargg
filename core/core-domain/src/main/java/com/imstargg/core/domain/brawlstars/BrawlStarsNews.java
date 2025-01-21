package com.imstargg.core.domain.brawlstars;

import java.time.OffsetDateTime;

public record BrawlStarsNews(
        String title,
        String linkUrl,
        OffsetDateTime publishDate
) {
}
