package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.v1.request.PlayerSearchRequest;
import com.imstargg.core.api.controller.v1.response.PlayerResponse;
import com.imstargg.core.api.controller.v1.response.PlayerSearchResponse;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.PlayerSearchService;
import com.imstargg.core.domain.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    private final PlayerSearchService playerSearchService;
    private final PlayerService playerService;

    public PlayerController(PlayerSearchService playerSearchService, PlayerService playerService) {
        this.playerSearchService = playerSearchService;
        this.playerService = playerService;
    }

    @GetMapping("/api/v1/search")
    public ResponseEntity<PlayerSearchResponse> search(
            @ModelAttribute @Validated PlayerSearchRequest request) {
        PlayerSearchResponse response = PlayerSearchResponse
                .from(playerSearchService.search(request.toParam()));
        return ResponseEntity.accepted().body(response);

    }

    @GetMapping("/api/v1/players/{tag}")
    public PlayerResponse get(@PathVariable String tag) {
        return PlayerResponse.from(playerService.get(new BrawlStarsTag(tag)));
    }
}
