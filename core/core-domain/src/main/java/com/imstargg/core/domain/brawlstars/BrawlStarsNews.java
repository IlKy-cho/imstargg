package com.imstargg.core.domain.brawlstars;

import java.time.LocalDateTime;

public record BrawlStarsNews(
        String title,
        String linkUrl,
        LocalDateTime publishDate
) {
}
