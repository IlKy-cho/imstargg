package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.request.PageRequest;
import com.imstargg.core.api.controller.v1.response.BattleResponse;
import com.imstargg.core.domain.BattleService;
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
    public ListResponse<BattleResponse> getBattles(
            @PathVariable String tag,
            @ModelAttribute @Validated PageRequest pageRequest) {
        return new ListResponse<>(
                battleService.getPlayerBattles(new BrawlStarsTag(tag), pageRequest.page())
                        .stream()
                        .map(BattleResponse::from)
                        .toList()
        );
    }
}
