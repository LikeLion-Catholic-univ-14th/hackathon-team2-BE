package org.example.hackathon_team2_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.AiGenerationResponse;
import org.example.hackathon_team2_be.dto.GenerationCreateRequest;
import org.example.hackathon_team2_be.dto.GenerationCreateResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.service.GenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generations")
public class GenerationController {

    private final GenerationService generationService;

    @PostMapping
    public ResponseEntity<GenerationCreateResponse> createGeneration(@RequestBody GenerationCreateRequest request) {
        return ResponseEntity.ok(
                generationService.createGeneration(request)
        );
    }

    @GetMapping("/{generationId}")
    public ResponseEntity<GenerationResponse> getGeneration(@PathVariable Long generationId) {
        return ResponseEntity.ok(
                generationService.getGeneration(generationId)
        );
    }

    @PostMapping("/{generationId}/result")
    public ResponseEntity<Void> receiveGenerationResult(
            @PathVariable Long generationId,
            @RequestBody AiGenerationResponse response
    ) {
        generationService.completeGeneration(generationId, response);

        return ResponseEntity.ok().build();
    }

}
