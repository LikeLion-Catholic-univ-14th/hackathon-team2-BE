package org.example.hackathon_team2_be.generation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.client.OpenAiClient;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.dto.OpenAiResultDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationService {

    private final OpenAiClient openAiClient;

    /**
     * AI 미래 제품 생성 함수 (OpenAI 연동 및 Fallback 반영)
     */
    public GenerationResponseDto generateFutureProduct(GenerationRequestDto request) {
        log.info("Starting future product generation. Request: {}", request);

        try {
            // OpenAI API 호출
            OpenAiResultDto openAiResult = openAiClient.generateProductDesign(request);

            log.info("Successfully generated future product with OpenAI: {}", openAiResult.getProductName());

            String imageUrl = (openAiResult.getImageUrl() != null && !openAiResult.getImageUrl().isBlank())
                    ? openAiResult.getImageUrl()
                    : "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop";

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

    /**
     * AI 생성 실패 시 반환되는 예비(Fallback) 응답
     */
    public GenerationResponseDto buildFallbackResponse(GenerationRequestDto request) {
        String baseName = extractProductName(request);
        String contextName = extractContextName(request);
        String dnaSummary = extractDnaSummary(request);

        return GenerationResponseDto.builder()
                .productName(baseName + " 2076")
                .category("Adaptive " + contextName + " Gear")
                .imageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop")
                .description(String.format(
                        "2076년 %s 환경에 맞춰 재탄생한 비세토스 패턴의 스마트 백팩. 선택하신 [%s] DNA가 결합되어 미래 라이프스타일에서도 완벽한 수납과 MCM 브랜드 헤리티지를 유지합니다.",
                        contextName, dnaSummary
                ))
                .build();
    }
}
