package org.example.hackathon_team2_be.generation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.service.GenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/generations")
@RequiredArgsConstructor
public class GenerationController {

    private final GenerationService generationService;

    @PostMapping("/{generationId}/result")
    public ResponseEntity<GenerationResponseDto> generateResult(
            @PathVariable Long generationId,
            @RequestBody GenerationRequestDto request
    ) {
        log.info("Received generation request for generationId: {}", generationId);
        try {
            GenerationResponseDto response = generationService.generateFutureProduct(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Unhandled Exception in GenerationController for generationId: {}. Returning Fallback response.", generationId, e);
            GenerationResponseDto fallbackResponse = generationService.buildFallbackResponse(request);
            return ResponseEntity.ok(fallbackResponse);
        }
    }
}
