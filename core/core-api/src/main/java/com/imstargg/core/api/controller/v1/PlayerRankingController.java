package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.response.PlayerRankingResponse;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.ranking.PlayerRankingService;
import com.imstargg.core.enums.Country;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerRankingController {

    private final PlayerRankingService playerRankingService;

    public PlayerRankingController(PlayerRankingService playerRankingService) {
        this.playerRankingService = playerRankingService;
    }

    @GetMapping("/api/v1/rankings/{country}/players")
    public ListResponse<PlayerRankingResponse> getPlayerRanking(@PathVariable Country country) {
        return new ListResponse<>(
                playerRankingService.getPlayerRanking(country).stream()
                        .map(PlayerRankingResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/rankings/{country}/brawlers/{brawlerBrawlStarsId}")
    public ListResponse<PlayerRankingResponse> getBrawlerRanking(
            @PathVariable Country country, @PathVariable long brawlerBrawlStarsId
    ) {
        return new ListResponse<>(
                playerRankingService.getBrawlerRanking(country, new BrawlStarsId(brawlerBrawlStarsId)).stream()
                        .map(PlayerRankingResponse::of)
                        .toList()
        );
    }
}
