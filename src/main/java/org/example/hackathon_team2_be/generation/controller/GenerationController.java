package org.example.hackathon_team2_be.generation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.dto.ApiResponse;
import org.example.hackathon_team2_be.dto.GenerationCreateRequest;
import org.example.hackathon_team2_be.dto.GenerationCreateResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.service.GenerationService;
import org.example.hackathon_team2_be.service.GenerationDbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/generations")
@RequiredArgsConstructor
public class GenerationController {

    private final GenerationService generationService;
    private final GenerationDbService generationDbService;

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

    //추가
    @PostMapping
    public ResponseEntity<ApiResponse<GenerationCreateResponse>> createGeneration(
            @RequestBody GenerationCreateRequest request
    ) {
        // 1. DB에 GENERATING 상태로 생성
        GenerationCreateResponse generation =
                generationDbService.createGeneration(request);

        try {
            // 2. AI가 사용할 요청 데이터로 변환
            GenerationRequestDto aiRequest =
                    generationDbService.createAiRequest(request);

            // 3. AI 생성
            GenerationResponseDto aiResponse =
                    generationService.generateFutureProduct(aiRequest);

            // 4. 결과를 DB에 저장
            generationDbService.completeGeneration(
                    generation.getGenerationId(),
                    aiResponse
            );

            // 5. 생성 ID 반환
            return ResponseEntity.ok(
                    ApiResponse.success(generation)
            );

        } catch (Exception e) {
            log.error(
                    "Generation failed. generationId={}",
                    generation.getGenerationId(),
                    e
            );

            return ResponseEntity.ok(
                    ApiResponse.success(generation)
            );
        }
    }

    @GetMapping("/{generationId}")
    public ResponseEntity<ApiResponse<GenerationResponse>> getGeneration(
            @PathVariable Long generationId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        generationDbService.getGeneration(generationId)
                )
        );
    }
}
