package com.imstargg.admin.controller;

import com.imstargg.admin.controller.request.NewPlayerRequest;
import com.imstargg.admin.domain.NewPlayerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewPlayerController {

    private final NewPlayerService newPlayerService;

    public NewPlayerController(NewPlayerService newPlayerService) {
        this.newPlayerService = newPlayerService;
    }

    @PostMapping("/admin/api/players")
    public void create(@Validated @RequestBody NewPlayerRequest request) {
        newPlayerService.create(request.tag());
    }
}
