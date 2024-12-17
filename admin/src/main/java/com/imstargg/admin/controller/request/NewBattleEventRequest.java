package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewBattleEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewBattleEventRequest(
        @NotNull Long brawlStarsId,
        @NotBlank String mapCode
) {

    public NewBattleEvent toNewBattleEvent() {
        return new NewBattleEvent(brawlStarsId(), mapCode());
    }
}
