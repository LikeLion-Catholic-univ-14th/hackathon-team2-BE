package org.example.hackathon_team2_be.generation;

import org.example.hackathon_team2_be.generation.controller.GenerationController;
import org.example.hackathon_team2_be.generation.dto.ContextDto;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.dto.ProductDto;
import org.example.hackathon_team2_be.generation.service.GenerationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerationControllerTest {

    private final GenerationService generationService = new GenerationService();
    private final GenerationController generationController = new GenerationController(generationService);

    @Test
    @DisplayName("POST /generations/{generationId}/result - 더미 응답 데이터 검증")
    void testGenerateResult() {
        // given
        Long generationId = 1L;
        GenerationRequestDto request = GenerationRequestDto.builder()
                .product(ProductDto.builder()
                        .name("MCM AERO STARK")
                        .description("MCM 아이코닉 백팩 클래식 아카이브")
                        .build())
                .lockedDna(List.of(
                        DnaDto.builder().name("Visetos").description("시그니처 모노그램 패턴").build(),
                        DnaDto.builder().name("Mobility").description("자유로운 이동 기능성").build()
                ))
                .futureContext(ContextDto.builder()
                        .name("Space Travel")
                        .description("무중력 이동과 행성 간 여행 환경")
                        .build())
                .build();

        // when
        var responseEntity = generationController.generateResult(generationId, request);

        // then
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        GenerationResponseDto body = responseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getProductName()).contains("MCM AERO STARK 2076");
        assertThat(body.getCategory()).contains("Adaptive Space Travel Gear");
        assertThat(body.getImageUrl()).isNotEmpty();
        assertThat(body.getDescription()).contains("Visetos");
    }
}
