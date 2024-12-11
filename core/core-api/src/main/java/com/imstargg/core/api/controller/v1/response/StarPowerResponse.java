package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.StarPower;

public record StarPowerResponse(
        long id,
        String name
) {

    public static StarPowerResponse from(StarPower starPower) {
        return new StarPowerResponse(
                starPower.id().value(),
                starPower.name()
        );
    }
}
