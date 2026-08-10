package org.example.hackathon_team2_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.*;
import org.example.hackathon_team2_be.service.GenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generations")
public class GenerationController {

    private final GenerationService generationService;

    @PostMapping
    public ResponseEntity<ApiResponse<GenerationCreateResponse>> createGeneration(
            @RequestBody GenerationCreateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        generationService.createGeneration(request)
                )
        );
    }

    @GetMapping("/{generationId}")
    public ResponseEntity<ApiResponse<GenerationResponse>> getGeneration(
            @PathVariable Long generationId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        generationService.getGeneration(generationId)
                )
        );
    }

    @PostMapping("/{generationId}/result")
    public ResponseEntity<Void> receiveGenerationResult(
            @PathVariable Long generationId,
            @RequestBody AiGenerationResponse response
    ) {
        generationService.completeGeneration(
                generationId,
                response
        );

        return ResponseEntity.ok().build();
    }

}
