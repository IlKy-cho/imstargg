package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.response.PlayerBrawlerResponse;
import com.imstargg.core.api.controller.v1.response.PlayerResponse;
import com.imstargg.core.api.controller.v1.response.RenewalStatusResponse;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/api/v1/players/{tag}")
    public PlayerResponse get(@PathVariable String tag) {
        return PlayerResponse.from(playerService.get(new BrawlStarsTag(tag)));
    }

    @GetMapping("/api/v1/players/{tag}/brawlers")
    public ListResponse<PlayerBrawlerResponse> getBrawlers(@PathVariable String tag) {
        return new ListResponse<>(
                playerService.getBrawlers(new BrawlStarsTag(tag))
                        .stream()
                        .map(PlayerBrawlerResponse::from)
                        .toList()
        );
    }

    @PostMapping("/api/v1/players/{tag}/renew")
    public ResponseEntity<Void> renew(@PathVariable String tag) {
        playerService.renew(new BrawlStarsTag(tag));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/api/v1/players/{tag}/renew-new")
    public ResponseEntity<Void> renewNew(@PathVariable String tag) {
        playerService.renewNew(new BrawlStarsTag(tag));
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/api/v1/players/{tag}/renewal-status")
    public RenewalStatusResponse getRenewalStatus(@PathVariable String tag) {
        return new RenewalStatusResponse(playerService.isRenewing(new BrawlStarsTag(tag)));
    }

    @GetMapping("/api/v1/players/{tag}/renewal-status-new")
    public RenewalStatusResponse getRenewalStatusNew(@PathVariable String tag) {
        return new RenewalStatusResponse(playerService.isRenewingNew(new BrawlStarsTag(tag)));
    }

}
