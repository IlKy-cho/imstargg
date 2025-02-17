package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.BattleEventUpdateRequest;
import com.imstargg.admin.controller.request.BrawlerUpdateRequest;
import com.imstargg.admin.controller.request.NewBrawlerRequest;
import com.imstargg.admin.controller.request.NewGearRequest;
import com.imstargg.admin.controller.response.ListResponse;
import com.imstargg.admin.domain.BattleEvent;
import com.imstargg.admin.domain.BattleService;
import com.imstargg.admin.domain.Brawler;
import com.imstargg.admin.domain.BrawlerService;
import com.imstargg.admin.domain.Gear;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BrawlStarsController {

    private final BrawlerService brawlerService;
    private final BattleService battleService;

    public BrawlStarsController(
            BrawlerService brawlerService,
            BattleService battleService
    ) {
        this.brawlerService = brawlerService;
        this.battleService = battleService;
    }

    @GetMapping("/admin/api/brawlers")
    public ListResponse<Brawler> getBrawlers() {
        return new ListResponse<>(brawlerService.getList());
    }

    @PostMapping("/admin/api/brawlers")
    public void registerBrawler(
            @Validated @RequestBody NewBrawlerRequest request) {
        brawlerService.register(request.toNewBrawler());
    }

    @PutMapping("/admin/api/brawlers/{brawlStarsId}")
    public void updateBrawler(
            @PathVariable long brawlStarsId,
            @Validated @RequestBody BrawlerUpdateRequest request
    ) {
        brawlerService.update(brawlStarsId, request.toBrawlerUpdate());
    }

    @PutMapping("/admin/api/brawlers/{brawlStarsId}/profile-image")
    public void uploadBrawlerProfileImage(
            @PathVariable long brawlStarsId, MultipartFile image) {
        brawlerService.uploadProfileImage(brawlStarsId, image.getResource());
    }

    @GetMapping("/admin/api/gears")
    public ListResponse<Gear> getGears() {
        return new ListResponse<>(brawlerService.getGearList());
    }

    @PostMapping("/admin/api/gears")
    public void registerGear(@Validated @RequestBody NewGearRequest request) {
        brawlerService.registerGear(request.toNewGear());
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

    @PutMapping("/admin/api/gadgets/{brawlStarsId}/image")
    public void uploadGadgetImage(
            @PathVariable long brawlStarsId, MultipartFile image) {
        brawlerService.uploadGadgetImage(brawlStarsId, image.getResource());
    }

    @PutMapping("/admin/api/starpowers/{brawlStarsId}/image")
    public void uploadStarPowerImage(
            @PathVariable long brawlStarsId, MultipartFile image) {
        brawlerService.uploadStarPowerImage(brawlStarsId, image.getResource());
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
