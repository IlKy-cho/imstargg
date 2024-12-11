package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.Gadget;

public record GadgetResponse(
        long id,
        String name
) {

    public static GadgetResponse from(Gadget gadget) {
        return new GadgetResponse(
                gadget.id().value(),
                gadget.name()
        );
    }
}
