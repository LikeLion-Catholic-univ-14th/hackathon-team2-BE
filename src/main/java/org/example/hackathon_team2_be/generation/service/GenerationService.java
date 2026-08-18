package org.example.hackathon_team2_be.generation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.client.OpenAiClient;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.dto.OpenAiResultDto;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationService {

    private final OpenAiClient openAiClient;

    // AI 미래 제품 생성 함수 (OpenAI 연동 및 Fallback)
    public GenerationResponseDto generateFutureProduct(GenerationRequestDto request) {
        log.info("Starting future product generation. Request: {}", request);

        try {
            // OpenAI API 호출
            OpenAiResultDto openAiResult = openAiClient.generateProductDesign(request);

            log.info("Successfully generated future product with OpenAI: {}", openAiResult.getProductName());

            String imageUrl = buildPollinationsImageUrl(openAiResult.getImagePrompt());

            return GenerationResponseDto.builder()
                    .productName(openAiResult.getProductName())
                    .category(openAiResult.getCategory())
                    .imageUrl(imageUrl)
                    .description(openAiResult.getDescription())
                    .build();

        } catch (Exception e) {
            log.error("Error occurred during AI product generation. Returning Fallback response.", e);
            return buildFallbackResponse(request);
        }
    }

    // 영문 묘사문(Prompt)을 기반으로 Pollinations AI (FLUX 모델) 실시간 이미지 URL 조합
    private String buildPollinationsImageUrl(String prompt) {
        String effectivePrompt = (prompt != null && !prompt.isBlank())
                ? prompt
                : "futuristic luxury MCM cyber backpack in year 2076 with glowing Visetos patterns, titanium hardware, 8k resolution, cinematic lighting";

        try {
            String encodedPrompt = URLEncoder.encode(effectivePrompt, StandardCharsets.UTF_8).replace("+", "%20");
            int seed = ThreadLocalRandom.current().nextInt(1, 1_000_000);
            return String.format(
                    "https://image.pollinations.ai/prompt/%s?model=flux&width=1024&height=1024&nologo=true&seed=%d",
                    encodedPrompt,
                    seed
            );
        } catch (Exception e) {
            log.warn("Failed to encode prompt for Pollinations AI, using fallback URL", e);
            return "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop";
        }
    }

    private String extractProductName(GenerationRequestDto request) {
        if (request != null && request.getProduct() != null && request.getProduct().getName() != null) {
            return request.getProduct().getName();
        }
        return "MCM ARCHIVE";
    }

    private String extractContextName(GenerationRequestDto request) {
        if (request != null && request.getFutureContext() != null && request.getFutureContext().getName() != null) {
            return request.getFutureContext().getName();
        }
        return "Space Travel";
    }

    private String extractDnaSummary(GenerationRequestDto request) {
        if (request != null && request.getLockedDna() != null && !request.getLockedDna().isEmpty()) {
            return request.getLockedDna().stream()
                    .map(DnaDto::getName)
                    .collect(Collectors.joining(", "));
        }
        return "Visetos, Mobility";
    }

    // Fallback
    public GenerationResponseDto buildFallbackResponse(GenerationRequestDto request) {
        String baseName = extractProductName(request);
        String contextName = extractContextName(request);
        String dnaSummary = extractDnaSummary(request);

        String fallbackPrompt = String.format(
                "futuristic luxury MCM %s in 2076 %s environment, %s, cyberpunk high fashion, 8k resolution, cinematic lighting",
                baseName, contextName, dnaSummary
        );
        String imageUrl = buildPollinationsImageUrl(fallbackPrompt);

        return GenerationResponseDto.builder()
                .productName(baseName + " 2076")
                .category("Adaptive " + contextName + " Gear")
                .imageUrl(imageUrl)
                .description(String.format(
                        "2076년 %s 환경에 맞춰 재탄생한 비세토스 패턴의 스마트 백팩. 선택하신 [%s] DNA가 결합되어 미래 라이프스타일에서도 완벽한 수납과 MCM 브랜드 헤리티지를 유지합니다.",
                        contextName, dnaSummary
                ))
                .build();
    }
}
