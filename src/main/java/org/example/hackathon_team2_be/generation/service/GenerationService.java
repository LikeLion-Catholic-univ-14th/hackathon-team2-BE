package org.example.hackathon_team2_be.generation.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class GenerationService {

    /**
     * AI 미래 제품 생성 함수 (더미 및 예외 처리 반영)
     */
    public GenerationResponseDto generateFutureProduct(GenerationRequestDto request) {
        log.info("Starting future product generation. Request: {}", request);

        try {
            // 1. 입력 데이터 검증 및 추출
            String productName = extractProductName(request);
            String contextName = extractContextName(request);
            String dnaSummary = extractDnaSummary(request);

            // TODO: 실제 AI (LLM + DALL-E / Flux / Imagen 등) API 연동 로직 적용 예정
            // 2. 더미 Mock 응답 반환
            return GenerationResponseDto.builder()
                    .productName(productName + " 2076")
                    .category("Adaptive " + contextName + " Gear")
                    .imageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop")
                    .description(String.format(
                            "2076년 %s 환경에 맞춰 재탄생한 비세토스 패턴의 스마트 백팩. 선택하신 [%s] DNA가 결합되어 미래 라이프스타일에서도 완벽한 수납과 MCM 브랜드 헤리티지를 유지합니다.",
                            contextName, dnaSummary
                    ))
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
        return GenerationResponseDto.builder()
                .productName(baseName + " 2076")
                .category("Future Mobility Gear")
                .imageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=1000&auto=format&fit=crop")
                .description("2076년 MCM 타임 포털을 통해 생성된 헤리티지 미래 에디션입니다.")
                .build();
    }
}
