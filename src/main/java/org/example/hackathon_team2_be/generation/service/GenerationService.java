package org.example.hackathon_team2_be.generation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.client.GeminiImageClient;
import org.example.hackathon_team2_be.generation.client.OpenAiClient;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.GenerationResponseDto;
import org.example.hackathon_team2_be.generation.dto.OpenAiResultDto;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Service;

import org.example.hackathon_team2_be.domain.PresetScenario;
import org.example.hackathon_team2_be.repository.PresetScenarioRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationService {

    private final OpenAiClient openAiClient;
    private final GeminiImageClient geminiImageClient;
    private final PresetScenarioRepository presetScenarioRepository;
    private final ScenarioImageResolver scenarioImageResolver;

    // AI 미래 제품 생성 함수 (90개 시나리오 이미지 매칭 + AI 기획 및 스마트 폴백)
    public GenerationResponseDto generateFutureProduct(GenerationRequestDto request) {
        log.info("Starting future product generation. Request: {}", request);

        OpenAiResultDto openAiResult = null;

        // 1. AI 디자인 기획 및 영문 프롬프트 생성 (텍스트 생성)
        try {
            openAiResult = openAiClient.generateProductDesign(request);
            log.info("Successfully generated future product design with AI: {}", openAiResult != null ? openAiResult.getProductName() : "null");
        } catch (Exception e) {
            log.warn("AI design planning failed. Returning fallback response: {}", e.getMessage());
            return buildFallbackResponse(request);
        }

        // 1단계 실패 시 전체 Fallback 반환
        if (openAiResult == null) {
            log.warn("AI design planning returned null. Returning fallback response.");
            return buildFallbackResponse(request);
        }

        // 2. 90개 사전 제작 이미지 매칭 (매칭 실패 시 Pollinations AI 실시간 생성)
        String imageUrl = resolveImageUrl(request, openAiResult.getImagePrompt());
        log.info("Resolved final product image URL: {}", imageUrl);

        // 3. 최종 응답 반환
        return GenerationResponseDto.builder()
                .productName(openAiResult.getProductName())
                .category(openAiResult.getCategory())
                .imageUrl(imageUrl)
                .description(openAiResult.getDescription())
                .build();
    }

    // 영문 묘사문(Prompt)을 기반으로 Pollinations AI (FLUX Realism 모델) 실시간 이미지 URL 조합
    private String buildPollinationsImageUrl(String prompt) {
        String effectivePrompt = (prompt != null && !prompt.isBlank())
                ? prompt
                : "Commercial studio packshot of an empty standalone evolved 2076 MCM Ottomar Weekender bag resting on a sleek minimalist white pedestal, strictly zero humans, no people, no models. Bright minimalist white studio backdrop with soft sky-blue and subtle violet pastel ambient lighting. The bag retains the classic Cognac Visetos luggage silhouette, enhanced with zero-gravity magnetic handles, carbon-fiber frame, and cyan glowing monogram seams, crisp focus, photorealistic, 8k --ar 1:1";

        try {
            String encodedPrompt = URLEncoder.encode(effectivePrompt, StandardCharsets.UTF_8).replace("+", "%20");
            int seed = ThreadLocalRandom.current().nextInt(1, 1_000_000);
            return String.format(
                    "https://image.pollinations.ai/prompt/%s?model=flux-realism&width=1024&height=1024&nologo=true&seed=%d",
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

    // 90개 시나리오 이미지 매칭 (무조건 90개 저장 이미지 중 최적의 파일 URL 100% 반환)
    private String resolveImageUrl(GenerationRequestDto request, String prompt) {
        if (scenarioImageResolver != null) {
            Optional<String> matchedImage = scenarioImageResolver.resolveScenarioImageUrl(request);
            if (matchedImage.isPresent()) {
                return matchedImage.get();
            }
        }
        return "/images/scenarios/01_Ottomar_Boston_weekender_travel_bag_VISETOS_Space_Travel.png";
    }

    // 특정 시나리오 프리셋 매핑 (DB 우선 조회)
    private GenerationResponseDto findPresetScenario(GenerationRequestDto request) {
        String productName = extractProductName(request);
        String contextName = extractContextName(request);

        // 1. DB (preset_scenarios 테이블)에서 시나리오 매칭 조회
        if (presetScenarioRepository != null) {
            try {
                Optional<PresetScenario> dbPreset = presetScenarioRepository.findByMatchingNames(productName, contextName);
                if (dbPreset.isPresent()) {
                    PresetScenario preset = dbPreset.get();
                    log.info("Matching preset scenario found in DB for [{} + {}]. Returning DB preset (imageUrl: {}).",
                            productName, contextName, preset.getImageUrl());
                    return GenerationResponseDto.builder()
                            .productName(preset.getProductName())
                            .category(preset.getCategory())
                            .imageUrl(preset.getImageUrl())
                            .description(preset.getDescription())
                            .build();
                }
            } catch (Exception e) {
                log.warn("Failed to query preset scenario from DB: {}", e.getMessage());
            }
        }

        return null;
    }

    // Fallback (AI 호출 실패 시에도 90개 사전 제작 이미지를 100% 매칭하여 즉시 반환)
    public GenerationResponseDto buildFallbackResponse(GenerationRequestDto request) {
        String baseName = extractProductName(request);
        String contextName = extractContextName(request);
        String dnaSummary = extractDnaSummary(request);

        String fallbackPrompt = String.format(
                "Commercial hero product shot of an empty standalone futuristic 2076 MCM %s levitating against a clean white studio backdrop glowing with soft purple and cyan ambient light, strictly zero humans, no models. The %s silhouette is enhanced for %s with carbon-fiber structure, subtle glowing Visetos monogram pattern, and titanium hardware, crisp focus, photorealistic, 8k --ar 1:1",
                baseName, dnaSummary, contextName
        );
        String imageUrl = resolveImageUrl(request, fallbackPrompt);

        return GenerationResponseDto.builder()
                .productName(baseName + " 2076")
                .category("Adaptive " + contextName + " Gear")
                .imageUrl(imageUrl)
                .description(String.format(
                        "2076년 %s 환경에 맞춰 재탄생한 비세토스 패턴의 스마트 가방. 선택하신 [%s] DNA가 결합되어 미래 라이프스타일에서도 완벽한 수납과 MCM 브랜드 헤리티지를 유지합니다.",
                        contextName, dnaSummary
                ))
                .build();
    }
}
