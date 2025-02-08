package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.StarPower;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;

public record StarPowerResponse(
        long id,
        String name,
        @Nullable String imagePath
) {

    public static StarPowerResponse from(StarPower starPower) {
        return new StarPowerResponse(
                starPower.id().value(),
                starPower.names()
                        .find(Language.KOREAN)
                        .orElseThrow(() -> new IllegalStateException(
                                "스타파워 이름이 존재하지 않습니다. starPower id: " + starPower.id().value()))
                        .content(),
                starPower.imagePath()
        );
    }
}
