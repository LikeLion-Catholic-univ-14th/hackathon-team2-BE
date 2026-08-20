package org.example.hackathon_team2_be.generation.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScenarioImageResolver {

    private final List<String> availableFileNames = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:static/images/scenarios/*.png");
            for (Resource resource : resources) {
                if (resource.getFilename() != null) {
                    availableFileNames.add(resource.getFilename());
                }
            }
            log.info("Initialized ScenarioImageResolver with {} scenario images.", availableFileNames.size());
        } catch (IOException e) {
            log.warn("Could not load scenario images from classpath: {}", e.getMessage());
        }
    }

    /**
     * 사용자가 선택한 제품, 미래 환경, 잠금 DNA 정보를 기반으로 90개 시나리오 이미지 중 최적의 이미지 URL 반환
     */
    public Optional<String> resolveScenarioImageUrl(GenerationRequestDto request) {
        if (request == null || availableFileNames.isEmpty()) {
            return Optional.empty();
        }

        String productKey = extractProductKeyword(request);
        String contextKey = extractContextKeyword(request);
        List<String> dnaKeys = extractDnaKeywords(request);

        log.info("Matching scenario image for Product='{}', Context='{}', DNAs={}", productKey, contextKey, dnaKeys);

        // 1. 완벽 매칭 시도 (Product + Context + 선택한 모든 DNA 포함)
        for (String fileName : availableFileNames) {
            String upperFileName = fileName.toUpperCase().replace(" ", "_");

            boolean productMatch = upperFileName.contains(productKey.toUpperCase());
            boolean contextMatch = upperFileName.contains(contextKey.toUpperCase().replace(" ", "_"));

            if (!productMatch || !contextMatch) {
                continue;
            }

            if (dnaKeys.isEmpty()) {
                return Optional.of("/images/scenarios/" + fileName);
            }

            boolean allDnaMatch = true;
            for (String dna : dnaKeys) {
                String normalizedDna = dna.toUpperCase().trim().replace(" ", "_");
                if (!upperFileName.contains(normalizedDna)) {
                    allDnaMatch = false;
                    break;
                }
            }

            if (allDnaMatch) {
                long dnaCountInFile = countDnaMatches(upperFileName, productKey);
                if (dnaCountInFile == dnaKeys.size()) {
                    log.info("Exact scenario image matched: {}", fileName);
                    return Optional.of("/images/scenarios/" + fileName);
                }
            }
        }

        // 2. 차선책 매칭 (가장 많은 DNA가 일치하는 파일 선택)
        String bestMatch = null;
        int maxScore = -1;

        for (String fileName : availableFileNames) {
            String upperFileName = fileName.toUpperCase().replace(" ", "_");
            boolean productMatch = upperFileName.contains(productKey.toUpperCase());
            boolean contextMatch = upperFileName.contains(contextKey.toUpperCase().replace(" ", "_"));

            if (!productMatch || !contextMatch) {
                continue;
            }

            int score = 0;
            for (String dna : dnaKeys) {
                String normalizedDna = dna.toUpperCase().trim().replace(" ", "_");
                if (upperFileName.contains(normalizedDna)) {
                    score++;
                }
            }

            if (score > maxScore) {
                maxScore = score;
                bestMatch = fileName;
            }
        }

        if (bestMatch != null) {
            log.info("Best-effort scenario image matched: {}", bestMatch);
            return Optional.of("/images/scenarios/" + bestMatch);
        }

        return Optional.empty();
    }

    private String extractProductKeyword(GenerationRequestDto request) {
        String name = (request.getProduct() != null && request.getProduct().getName() != null)
                ? request.getProduct().getName().toLowerCase() : "";
        if (name.contains("ottomar") || name.contains("위켄더") || name.contains("boston")) {
            return "Ottomar";
        }
        if (name.contains("stark") || name.contains("스타크") || name.contains("백팩") || name.contains("backpack") || name.contains("aero")) {
            return "Stark";
        }
        if (name.contains("pouch") || name.contains("파우치") || name.contains("crossbody") || name.contains("best")) {
            return "crossbody_pouch";
        }
        return "Stark";
    }

    private String extractContextKeyword(GenerationRequestDto request) {
        String name = (request.getFutureContext() != null && request.getFutureContext().getName() != null)
                ? request.getFutureContext().getName().toLowerCase() : "";
        if (name.contains("space") || name.contains("우주")) {
            return "Space_Travel";
        }
        if (name.contains("city") || name.contains("도시") || name.contains("hyper")) {
            return "Hyper_City";
        }
        if (name.contains("virtual") || name.contains("가상") || name.contains("dimension")) {
            return "Virtual_Dimension";
        }
        return "Space_Travel";
    }

    private List<String> extractDnaKeywords(GenerationRequestDto request) {
        if (request.getLockedDna() == null) {
            return Collections.emptyList();
        }
        return request.getLockedDna().stream()
                .map(DnaDto::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private long countDnaMatches(String upperFileName, String productKey) {
        List<String> candidateDnas;
        if ("Ottomar".equalsIgnoreCase(productKey)) {
            candidateDnas = List.of("VISETOS", "MOBILITY", "COGNAC_COLOR", "GEOMETRIC_STRUCTURE");
        } else if ("Stark".equalsIgnoreCase(productKey)) {
            candidateDnas = List.of("VISETOS", "VISIBLE_IDENTITY", "METAL_STUDS", "MOBILITY");
        } else {
            candidateDnas = List.of("VISETOS", "CULTURAL_COLLABORATION", "MIAMI_BLUE", "ADAPTIVE_STYLING");
        }
        return candidateDnas.stream().filter(upperFileName::contains).count();
    }
}