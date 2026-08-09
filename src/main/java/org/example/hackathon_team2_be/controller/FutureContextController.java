package org.example.hackathon_team2_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.FutureContextListResponse;
import org.example.hackathon_team2_be.service.FutureContextService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/future-contexts")
public class FutureContextController {

    private final FutureContextService futureContextService;

    @GetMapping
    public FutureContextListResponse getFutureContexts() {
        return futureContextService.getFutureContexts();
    }
}
