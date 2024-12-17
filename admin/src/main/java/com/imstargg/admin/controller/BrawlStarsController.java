package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.BrawlerUpdateRequest;
import com.imstargg.admin.controller.request.NewBrawlerRequest;
import com.imstargg.admin.controller.request.NewBattleMapRequest;
import com.imstargg.admin.domain.BattleMapService;
import com.imstargg.admin.domain.BrawlerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BrawlStarsController {

    private final BrawlerService brawlerService;
    private final BattleMapService mapService;

    public BrawlStarsController(
            BrawlerService brawlerService,
            BattleMapService mapService
    ) {
        this.brawlerService = brawlerService;
        this.mapService = mapService;
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

    @PostMapping("/admin/api/maps")
    public void registerMap(@Validated @RequestBody NewBattleMapRequest request) {
        mapService.register(request.toNewBattleMap());
    }

    @PutMapping("/admin/api/maps/{mapCode}/image")
    public void uploadMapImage(
            @PathVariable String mapCode, MultipartFile image) {
        mapService.uploadImage(mapCode, image.getResource());
    }
}
