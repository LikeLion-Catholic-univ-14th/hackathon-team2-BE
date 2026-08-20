package org.example.hackathon_team2_be.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hackathon_team2_be.generation.client.GeminiImageClient;
import org.example.hackathon_team2_be.generation.client.OpenAiClient;
import org.example.hackathon_team2_be.generation.controller.GenerationController;
import org.example.hackathon_team2_be.generation.dto.ContextDto;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.dto.ProductDto;
import org.example.hackathon_team2_be.generation.service.GenerationService;
import org.example.hackathon_team2_be.generation.service.ScenarioImageResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerationControllerTest {

    private final OpenAiClient openAiClient = new OpenAiClient("", "https://generativelanguage.googleapis.com/v1beta/openai", "gemini-1.5-flash", new ObjectMapper());
    private final GeminiImageClient geminiImageClient = new GeminiImageClient("", "https://generativelanguage.googleapis.com/v1beta", "gemini-2.5-flash-image", new ObjectMapper());
    private final ScenarioImageResolver scenarioImageResolver = new ScenarioImageResolver();
    private final GenerationService generationService = new GenerationService(openAiClient, geminiImageClient, null, scenarioImageResolver);
    private final GenerationController generationController = new GenerationController(generationService, null);

    @Test
    @DisplayName("POST /generations/{generationId}/result - 90개 시나리오 이미지 매칭 응답 데이터 검증")
    void testGenerateResult() {
        // given
        Long generationId = 1L;
        GenerationRequestDto request = GenerationRequestDto.builder()
                .product(ProductDto.builder()
                        .name("MCM AERO STARK")
                        .description("MCM 아이코닉 백팩 클래식 아카이브")
                        .build())
                .lockedDna(List.of(
                        DnaDto.builder().name("VISETOS").description("시그니처 모노그램 패턴").build(),
                        DnaDto.builder().name("MOBILITY").description("자유로운 이동 기능성").build()
                ))
                .futureContext(ContextDto.builder()
                        .name("Space Travel")
                        .description("무중력 이동과 행성 간 여행 환경")
                        .build())
                .build();

        // when
        GenerationResponseDto body = generationService.generateFutureProduct(request);

        // then
        assertThat(body).isNotNull();
        assertThat(body.getProductName()).contains("MCM AERO STARK");
        assertThat(body.getCategory()).isNotEmpty();
        assertThat(body.getImageUrl()).contains("37_Stark_backpack_VISETOS_MOBILITY_Space_Travel.png");
        assertThat(body.getDescription()).isNotEmpty();
    }
}
