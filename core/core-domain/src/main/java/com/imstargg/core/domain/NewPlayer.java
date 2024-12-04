package com.imstargg.core.domain;

import com.imstargg.core.enums.UnknownPlayerStatus;

import java.time.LocalDateTime;

public record NewPlayer(
        BrawlStarsTag tag,
        UnknownPlayerStatus status,
        LocalDateTime updateAvailableAt
) {
}
