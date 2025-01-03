package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.BrawlerUpdateRequest;
import com.imstargg.admin.controller.request.NewBattleEventRequest;
import com.imstargg.admin.controller.request.NewBattleMapRequest;
import com.imstargg.admin.controller.request.NewBrawlerRequest;
import com.imstargg.admin.controller.request.NewGearRequest;
import com.imstargg.admin.controller.response.ListResponse;
import com.imstargg.admin.domain.BattleEvent;
import com.imstargg.admin.domain.BattleMap;
import com.imstargg.admin.domain.BattleService;
import com.imstargg.admin.domain.Brawler;
import com.imstargg.admin.domain.BrawlerService;
import com.imstargg.admin.domain.Gear;
import com.imstargg.admin.domain.NotRegisteredBattleEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/admin/api/maps")
    public ListResponse<BattleMap> getMaps() {
        return new ListResponse<>(battleService.getMapList());
    }

    @PostMapping("/admin/api/maps")
    public void registerMap(@Validated @RequestBody NewBattleMapRequest request) {
        battleService.registerMap(request.toNewBattleMap());
    }

    @PutMapping("/admin/api/maps/{mapCode}/image")
    public void uploadMapImage(
            @PathVariable String mapCode, MultipartFile image) {
        battleService.uploadMapImage(mapCode, image.getResource());
    }

    @GetMapping("/admin/api/events")
    public ListResponse<BattleEvent> getEvents() {
        return new ListResponse<>(battleService.getEventList());
    }

    @GetMapping("/admin/api/not-registered-events")
    public ListResponse<NotRegisteredBattleEvent> getNotRegisteredEvents() {
        return new ListResponse<>(battleService.getNotRegisteredEventList());
    }

    @PostMapping("/admin/api/events")
    public void registerEvent(@Validated @RequestBody NewBattleEventRequest request) {
        battleService.registerEvent(request.toNewBattleEvent());
    }

    @PutMapping("/admin/api/events/{eventId}/season")
    public void eventSeasoned(@PathVariable long eventId) {
        battleService.eventSeasoned(eventId);
    }

    @DeleteMapping("/admin/api/events/{eventId}/season")
    public void eventUnseasoned(@PathVariable long eventId) {
        battleService.eventUnseasoned(eventId);
    }

    @DeleteMapping("/admin/api/events/{eventId}")
    public void deleteEvent(@PathVariable long eventId) {
        battleService.deleteEvent(eventId);
    }

    @PostMapping("/admin/api/events/{eventId}/restore")
    public void restoreEvent(@PathVariable long eventId) {
        battleService.restoreEvent(eventId);
    }
}
