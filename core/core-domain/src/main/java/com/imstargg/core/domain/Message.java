package com.imstargg.core.domain;

import com.imstargg.core.enums.Language;

public record Message(
        Language language,
        String content
) {
}
