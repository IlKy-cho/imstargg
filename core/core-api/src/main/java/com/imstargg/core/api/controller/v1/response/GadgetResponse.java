package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gadget;
import jakarta.annotation.Nullable;

public record GadgetResponse(
        long id,
        String name,
        @Nullable String imagePath
) {

    public static GadgetResponse from(Gadget gadget) {
        return new GadgetResponse(
                gadget.id().value(),
                gadget.name(),
                gadget.imagePath()
        );
    }
}
