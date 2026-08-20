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

    // AI 백엔드 → 메인 백엔드
    // AI 생성 결과 전달
    @PostMapping("/{generationId}/result")
    public ResponseEntity<Void> receiveGenerationResult(
            @PathVariable Long generationId,
            @RequestBody GenerationResponseDto response
    ) {
        log.info(
                "Received AI generation result. generationId={}",
                generationId
        );

        generationDbService.completeGeneration(
                generationId,
                response
        );

        return ResponseEntity.ok().build();
    }


        //추가
        @PostMapping
        public ResponseEntity<ApiResponse<GenerationCreateResponse>> createGeneration(
                @RequestBody GenerationCreateRequest request
        ) {
            GenerationCreateResponse generation =
                    generationDbService.createGeneration(request);

            try {
                GenerationRequestDto aiRequest =
                        generationDbService.createAiRequest(request);

                GenerationResponseDto aiResponse =
                        generationService.generateFutureProduct(aiRequest);

                generationDbService.completeGeneration(
                        generation.getGenerationId(),
                        aiResponse
                );

                return ResponseEntity.ok(
                        ApiResponse.success(generation)
                );

            } catch (Exception e) {
                log.error(
                        "Generation failed. generationId={}",
                        generation.getGenerationId(),
                        e
                );

                generationDbService.failGeneration(
                        generation.getGenerationId()
                );

                return ResponseEntity.ok(
                        ApiResponse.success(generation)
                );
            }
        }

    // 생성 결과 조회
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
