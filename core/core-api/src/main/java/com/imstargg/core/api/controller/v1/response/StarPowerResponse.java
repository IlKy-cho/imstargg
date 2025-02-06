package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.StarPower;
import jakarta.annotation.Nullable;

public record StarPowerResponse(
        long id,
        String name,
        @Nullable String imagePath
) {

    public static StarPowerResponse from(StarPower starPower) {
        return new StarPowerResponse(
                starPower.id().value(),
                starPower.name(),
                starPower.imagePath()
        );
    }
}
