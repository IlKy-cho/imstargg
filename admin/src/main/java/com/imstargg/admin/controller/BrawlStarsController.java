package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.NewBrawlerRequest;
import com.imstargg.admin.domain.BrawlerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrawlStarsController {

    private final BrawlerService brawlerService;

    public BrawlStarsController(BrawlerService brawlerService) {
        this.brawlerService = brawlerService;
    }

    @PostMapping("/admin/api/brawlers")
    public void register(@Validated @RequestBody NewBrawlerRequest request) {
        brawlerService.register(request.toNewBrawler());
    }
}
