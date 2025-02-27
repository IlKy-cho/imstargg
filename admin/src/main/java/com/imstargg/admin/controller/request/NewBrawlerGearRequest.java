package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewBrawlerGear;

public record NewBrawlerGearRequest(
        long brawlStarsId
) {

    public NewBrawlerGear toNewBrawlerGear() {
        return new NewBrawlerGear(brawlStarsId);
    }
}
