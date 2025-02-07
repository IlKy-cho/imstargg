package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.v1.response.ClubResponse;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.club.ClubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/api/v1/clubs/{tag}")
    public ClubResponse get(@PathVariable String tag) {
        return ClubResponse.from(clubService.get(new BrawlStarsTag(tag)));
    }
}
