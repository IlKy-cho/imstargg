package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gadget;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;

public record GadgetResponse(
        long id,
        String name,
        @Nullable String imagePath
) {

    public static GadgetResponse from(Gadget gadget) {
        return new GadgetResponse(
                gadget.id().value(),
                gadget.names()
                        .find(Language.KOREAN)
                        .orElseThrow(() -> new IllegalStateException(
                                "가젯 이름이 존재하지 않습니다. gadget id: " + gadget.id().value()))
                        .content(),
                gadget.imagePath()
        );
    }
}
