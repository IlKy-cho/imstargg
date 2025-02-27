package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.BattleEventUpdateRequest;
import com.imstargg.admin.controller.response.ListResponse;
import com.imstargg.admin.domain.BattleEvent;
import com.imstargg.admin.domain.BattleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PutMapping("/admin/api/events/{brawlStarsId}/image")
    public void uploadMapImage(
            @PathVariable long brawlStarsId, MultipartFile image) {
        battleService.uploadMapImage(brawlStarsId, image.getResource());
    }

    @GetMapping("/admin/api/events")
    public ListResponse<BattleEvent> getEvents() {
        return new ListResponse<>(battleService.getEventList());
    }

    @PatchMapping("/admin/api/events/{brawlStarsId}")
    public void updateEvent(
            @PathVariable long brawlStarsId,
            @RequestBody @Validated BattleEventUpdateRequest request
    ) {
        battleService.updateBattleEvent(brawlStarsId, request.toBattleEventUpdate());
    }

    @PutMapping("/admin/api/events/{brawlStarsId}/solo-rank")
    public void registerSoloRankBattleEvent(
            @PathVariable long brawlStarsId
    ) {
        battleService.registerSoloRankBattleEvent(brawlStarsId);
    }

    @DeleteMapping("/admin/api/events/{brawlStarsId}/solo-rank")
    public void deleteSoloRankBattleEvent(
            @PathVariable long brawlStarsId
    ) {
        battleService.deleteSoloRankBattleEvent(brawlStarsId);
    }

}
