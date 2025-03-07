package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.SliceResponse;
import com.imstargg.core.api.controller.request.PageRequest;
import com.imstargg.core.api.controller.v1.response.PlayerBattleResponse;
import com.imstargg.core.domain.player.BattleService;
import com.imstargg.core.domain.BrawlStarsTag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping("/api/v1/players/{tag}/battles")
    public SliceResponse<PlayerBattleResponse> getBattles(
            @PathVariable String tag,
            @ModelAttribute @Validated PageRequest pageRequest) {
        return SliceResponse.of(
                battleService.getPlayerBattles(new BrawlStarsTag(tag), pageRequest.page())
                        .map(PlayerBattleResponse::of)
        );
    }
}
