package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;

public record BrawlStarsNewsPageParam(
        Language language,
        int page
) {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 6;

    public BrawlStarsNewsPageParam(Language language, Integer page) {
        this(
                language,
                page != null ? page : DEFAULT_PAGE
        );
    }

    public int size() {
        return DEFAULT_SIZE;
    }
}
