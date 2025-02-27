package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.BrawlerUpdateRequest;
import com.imstargg.admin.controller.request.GearUpdateRequest;
import com.imstargg.admin.controller.request.NewBrawlerGearRequest;
import com.imstargg.admin.controller.request.NewBrawlerRequest;
import com.imstargg.admin.controller.request.NewGadgetRequest;
import com.imstargg.admin.controller.request.NewGearRequest;
import com.imstargg.admin.controller.request.NewStarPowerRequest;
import com.imstargg.admin.controller.response.ListResponse;
import com.imstargg.admin.domain.Brawler;
import com.imstargg.admin.domain.BrawlerService;
import com.imstargg.admin.domain.Gear;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BrawlerController {

    private final BrawlerService brawlerService;

    public BrawlerController(
            BrawlerService brawlerService
    ) {
        this.brawlerService = brawlerService;
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

    @PutMapping("/admin/api/gears/{brawlStarsId}")
    public void updateGear(
            @PathVariable long brawlStarsId,
            @Validated @RequestBody GearUpdateRequest request
    ) {
        brawlerService.updateGear(brawlStarsId, request.toGearUpdate());
    }

    @PutMapping("/admin/api/brawlers/{brawlStarsId}/gears")
    public void registerBrawlerGear(
            @PathVariable long brawlStarsId, @Validated @RequestBody NewBrawlerGearRequest request) {
        brawlerService.registerBrawlerGear(brawlStarsId, request.toNewBrawlerGear());
    }

    @PostMapping("/admin/api/brawlers/{brawlStarsId}/star-powers")
    public void registerStarPower(
            @PathVariable long brawlStarsId, @Validated @RequestBody NewStarPowerRequest request) {
        brawlerService.registerStarPower(brawlStarsId, request.toNewStarPower());
    }

    @PostMapping("/admin/api/brawlers/{brawlStarsId}/gadgets")
    public void registerGadget(
            @PathVariable long brawlStarsId, @Validated @RequestBody NewGadgetRequest request) {
        brawlerService.registerGadget(brawlStarsId, request.toNewGadget());
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
}
