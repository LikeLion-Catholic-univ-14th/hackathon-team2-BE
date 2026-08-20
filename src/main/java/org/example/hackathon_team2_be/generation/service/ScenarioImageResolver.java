package org.example.hackathon_team2_be.generation.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScenarioImageResolver {

    // 90가지 시나리오 고화질 이미지 파일명 전체 정적 등록 (JAR 환경 스캔 실패 원천 차단)
    private static final List<String> ALL_SCENARIOS = List.of(
            "01_Ottomar_Boston_weekender_travel_bag_VISETOS_Space_Travel.png",
            "02_Ottomar_Boston_weekender_travel_bag_MOBILITY_Space_Travel.png",
            "03_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_Space_Travel.png",
            "04_Ottomar_Boston_weekender_travel_bag_GEOMETRIC STRUCTURE_Space_Travel.png",
            "05_Ottomar_Boston_weekender_travel_bag_VISETOS_MOBILITY_Space_Travel.png",
            "06_Ottomar_Boston_weekender_travel_bag_VISETOS_COGNAC COLOR_Space_Travel.png",
            "07_Ottomar_Boston_weekender_travel_bag_VISETOS_GEOMETRIC STRUCTURE_Space_Travel.png",
            "08_Ottomar_Boston_weekender_travel_bag_MOBILITY_COGNAC COLOR_Space_Travel.png",
            "09_Ottomar_Boston_weekender_travel_bag_MOBILITY_GEOMETRIC STRUCTURE_Space_Travel.png",
            "10_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_GEOMETRIC STRUCTURE_Space_Travel.png",
            "11_Ottomar_Boston_weekender_travel_bag_VISETOS_Hyper_City.png",
            "12_Ottomar_Boston_weekender_travel_bag_MOBILITY_Hyper_City.png",
            "13_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_Hyper_City.png",
            "14_Ottomar_Boston_weekender_travel_bag_GEOMETRIC STRUCTURE_Hyper_City.png",
            "15_Ottomar_Boston_weekender_travel_bag_VISETOS_MOBILITY_Hyper_City.png",
            "16_Ottomar_Boston_weekender_travel_bag_VISETOS_COGNAC COLOR_Hyper_City.png",
            "17_Ottomar_Boston_weekender_travel_bag_VISETOS_GEOMETRIC STRUCTURE_Hyper_City.png",
            "18_Ottomar_Boston_weekender_travel_bag_MOBILITY_COGNAC COLOR_Hyper_City.png",
            "19_Ottomar_Boston_weekender_travel_bag_MOBILITY_GEOMETRIC STRUCTURE_Hyper_City.png",
            "20_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_GEOMETRIC STRUCTURE_Hyper_City.png",
            "21_Ottomar_Boston_weekender_travel_bag_VISETOS_Virtual_Dimension.png",
            "22_Ottomar_Boston_weekender_travel_bag_MOBILITY_Virtual_Dimension.png",
            "23_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_Virtual_Dimension.png",
            "24_Ottomar_Boston_weekender_travel_bag_GEOMETRIC STRUCTURE_Virtual_Dimension.png",
            "25_Ottomar_Boston_weekender_travel_bag_VISETOS_MOBILITY_Virtual_Dimension.png",
            "26_Ottomar_Boston_weekender_travel_bag_VISETOS_COGNAC COLOR_Virtual_Dimension.png",
            "27_Ottomar_Boston_weekender_travel_bag_VISETOS_GEOMETRIC STRUCTURE_Virtual_Dimension.png",
            "28_Ottomar_Boston_weekender_travel_bag_MOBILITY_COGNAC COLOR_Virtual_Dimension.png",
            "29_Ottomar_Boston_weekender_travel_bag_MOBILITY_GEOMETRIC STRUCTURE_Virtual_Dimension.png",
            "30_Ottomar_Boston_weekender_travel_bag_COGNAC COLOR_GEOMETRIC STRUCTURE_Virtual_Dimension.png",
            "31_Stark_backpack_VISETOS_Space_Travel.png",
            "32_Stark_backpack_VISIBLE IDENTITY_Space_Travel.png",
            "33_Stark_backpack_METAL STUDS_Space_Travel.png",
            "34_Stark_backpack_MOBILITY_Space_Travel.png",
            "35_Stark_backpack_VISETOS_VISIBLE IDENTITY_Space_Travel.png",
            "36_Stark_backpack_VISETOS_METAL STUDS_Space_Travel.png",
            "37_Stark_backpack_VISETOS_MOBILITY_Space_Travel.png",
            "38_Stark_backpack_VISIBLE IDENTITY_METAL STUDS_Space_Travel.png",
            "39_Stark_backpack_VISIBLE IDENTITY_MOBILITY_Space_Travel.png",
            "40_Stark_backpack_METAL STUDS_MOBILITY_Space_Travel.png",
            "41_Stark_backpack_VISETOS_Hyper_City.png",
            "42_Stark_backpack_VISIBLE IDENTITY_Hyper_City.png",
            "43_Stark_backpack_METAL STUDS_Hyper_City.png",
            "44_Stark_backpack_MOBILITY_Hyper_City.png",
            "45_Stark_backpack_VISETOS_VISIBLE IDENTITY_Hyper_City.png",
            "46_Stark_backpack_VISETOS_METAL STUDS_Hyper_City.png",
            "47_Stark_backpack_VISETOS_MOBILITY_Hyper_City.png",
            "48_Stark_backpack_VISIBLE IDENTITY_METAL STUDS_Hyper_City.png",
            "49_Stark_backpack_VISIBLE IDENTITY_MOBILITY_Hyper_City.png",
            "50_Stark_backpack_METAL STUDS_MOBILITY_Hyper_City.png",
            "51_Stark_backpack_VISETOS_Virtual_Dimension.png",
            "52_Stark_backpack_VISIBLE IDENTITY_Virtual_Dimension.png",
            "53_Stark_backpack_METAL STUDS_Virtual_Dimension.png",
            "54_Stark_backpack_MOBILITY_Virtual_Dimension.png",
            "55_Stark_backpack_VISETOS_VISIBLE IDENTITY_Virtual_Dimension.png",
            "56_Stark_backpack_VISETOS_METAL STUDS_Virtual_Dimension.png",
            "57_Stark_backpack_VISETOS_MOBILITY_Virtual_Dimension.png",
            "58_Stark_backpack_VISIBLE IDENTITY_METAL STUDS_Virtual_Dimension.png",
            "59_Stark_backpack_VISIBLE IDENTITY_MOBILITY_Virtual_Dimension.png",
            "60_Stark_backpack_METAL STUDS_MOBILITY_Virtual_Dimension.png",
            "61_crossbody_pouch_bag_VISETOS_Space_Travel.png",
            "62_crossbody_pouch_bag_CULTURAL COLLABORATION_Space_Travel.png",
            "63_crossbody_pouch_bag_MIAMI BLUE_Space_Travel.png",
            "64_crossbody_pouch_bag_ADAPTIVE STYLING_Space_Travel.png",
            "65_crossbody_pouch_bag_VISETOS_CULTURAL COLLABORATION_Space_Travel.png",
            "66_crossbody_pouch_bag_VISETOS_MIAMI BLUE_Space_Travel.png",
            "67_crossbody_pouch_bag_VISETOS_ADAPTIVE STYLING_Space_Travel.png",
            "68_crossbody_pouch_bag_CULTURAL COLLABORATION_MIAMI BLUE_Space_Travel.png",
            "69_crossbody_pouch_bag_CULTURAL COLLABORATION_ADAPTIVE STYLING_Space_Travel.png",
            "70_crossbody_pouch_bag_MIAMI BLUE_ADAPTIVE STYLING_Space_Travel.png",
            "71_crossbody_pouch_bag_VISETOS_Hyper_City.png",
            "72_crossbody_pouch_bag_CULTURAL COLLABORATION_Hyper_City.png",
            "73_crossbody_pouch_bag_MIAMI BLUE_Hyper_City.png",
            "74_crossbody_pouch_bag_ADAPTIVE STYLING_Hyper_City.png",
            "75_crossbody_pouch_bag_VISETOS_CULTURAL COLLABORATION_Hyper_City.png",
            "76_crossbody_pouch_bag_VISETOS_MIAMI BLUE_Hyper_City.png",
            "77_crossbody_pouch_bag_VISETOS_ADAPTIVE STYLING_Hyper_City.png",
            "78_crossbody_pouch_bag_CULTURAL COLLABORATION_MIAMI BLUE_Hyper_City.png",
            "79_crossbody_pouch_bag_CULTURAL COLLABORATION_ADAPTIVE STYLING_Hyper_City.png",
            "80_crossbody_pouch_bag_MIAMI BLUE_ADAPTIVE STYLING_Hyper_City.png",
            "81_crossbody_pouch_bag_VISETOS_Virtual_Dimension.png",
            "82_crossbody_pouch_bag_CULTURAL COLLABORATION_Virtual_Dimension.png",
            "83_crossbody_pouch_bag_MIAMI BLUE_Virtual_Dimension.png",
            "84_crossbody_pouch_bag_ADAPTIVE STYLING_Virtual_Dimension.png",
            "85_crossbody_pouch_bag_VISETOS_CULTURAL COLLABORATION_Virtual_Dimension.png",
            "86_crossbody_pouch_bag_VISETOS_MIAMI BLUE_Virtual_Dimension.png",
            "87_crossbody_pouch_bag_VISETOS_ADAPTIVE STYLING_Virtual_Dimension.png",
            "88_crossbody_pouch_bag_CULTURAL COLLABORATION_MIAMI BLUE_Virtual_Dimension.png",
            "89_crossbody_pouch_bag_CULTURAL COLLABORATION_ADAPTIVE STYLING_Virtual_Dimension.png",
            "90_crossbody_pouch_bag_MIAMI BLUE_ADAPTIVE STYLING_Virtual_Dimension.png"
    );

    /**
     * 사용자가 선택한 제품, 미래 환경, 잠금 DNA 정보를 기반으로 90개 시나리오 이미지 중 최적의 이미지 URL 반환 (100% 보장)
     */
    public Optional<String> resolveScenarioImageUrl(GenerationRequestDto request) {
        if (request == null) {
            return Optional.of("/images/scenarios/" + ALL_SCENARIOS.get(0));
        }

        String productKey = extractProductKeyword(request);
        String contextKey = extractContextKeyword(request);
        List<String> dnaKeys = extractDnaKeywords(request);

        log.info("Matching scenario image for Product='{}', Context='{}', DNAs={}", productKey, contextKey, dnaKeys);

        // 1. 완벽 매칭 시도 (Product + Context + 선택한 모든 DNA 포함 & DNA 개수 일치)
        for (String fileName : ALL_SCENARIOS) {
            String upperFileName = normalize(fileName);

            boolean productMatch = upperFileName.contains(normalize(productKey));
            boolean contextMatch = upperFileName.contains(normalize(contextKey));

            if (!productMatch || !contextMatch) {
                continue;
            }

            if (dnaKeys.isEmpty()) {
                return Optional.of(buildImageUrl(fileName));
            }

            boolean allDnaMatch = true;
            for (String dna : dnaKeys) {
                if (!upperFileName.contains(normalize(dna))) {
                    allDnaMatch = false;
                    break;
                }
            }

            if (allDnaMatch) {
                long dnaCountInFile = countDnaMatches(upperFileName, productKey);
                if (dnaCountInFile == dnaKeys.size()) {
                    log.info("Exact scenario image matched: {}", fileName);
                    return Optional.of(buildImageUrl(fileName));
                }
            }
        }

        // 2. DNA 부분 매칭 (가장 많은 DNA가 일치하는 파일 선택)
        String bestMatch = null;
        int maxScore = -1;

        for (String fileName : ALL_SCENARIOS) {
            String upperFileName = normalize(fileName);
            boolean productMatch = upperFileName.contains(normalize(productKey));
            boolean contextMatch = upperFileName.contains(normalize(contextKey));

            if (!productMatch || !contextMatch) {
                continue;
            }

            int score = 0;
            for (String dna : dnaKeys) {
                if (upperFileName.contains(normalize(dna))) {
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
            return Optional.of(buildImageUrl(bestMatch));
        }

        // 3. Fallback: 해당 제품+환경의 첫 번째 파일 무조건 반환
        for (String fileName : ALL_SCENARIOS) {
            String upperFileName = normalize(fileName);
            if (upperFileName.contains(normalize(productKey)) && upperFileName.contains(normalize(contextKey))) {
                log.info("Product/Context fallback scenario image matched: {}", fileName);
                return Optional.of(buildImageUrl(fileName));
            }
        }

        return Optional.of(buildImageUrl(ALL_SCENARIOS.get(0)));
    }

    // 파일명을 변경하지 않고도 웹 브라우저에서 스페이스바(공백)를 안전하게 인식할 수 있도록 URL 인코딩
    private String buildImageUrl(String fileName) {
        if (fileName == null) return "/images/scenarios/01_Ottomar_Boston_weekender_travel_bag_VISETOS_Space_Travel.png";
        return "/images/scenarios/" + fileName.replace(" ", "%20");
    }

    private String normalize(String str) {
        if (str == null) return "";
        return str.toUpperCase().replaceAll("[^A-Z0-9]", "_");
    }

    private String extractProductKeyword(GenerationRequestDto request) {
        String name = (request.getProduct() != null && request.getProduct().getName() != null)
                ? request.getProduct().getName().toLowerCase() : "";
        if (name.contains("ottomar") || name.contains("위켄더") || name.contains("boston") || name.contains("오토마르")) {
            return "Ottomar";
        }
        if (name.contains("stark") || name.contains("스타크") || name.contains("백팩") || name.contains("backpack") || name.contains("aero")) {
            return "Stark";
        }
        if (name.contains("pouch") || name.contains("파우치") || name.contains("crossbody") || name.contains("best") || name.contains("크로스바디")) {
            return "crossbody_pouch";
        }
        return "Stark";
    }

    private String extractContextKeyword(GenerationRequestDto request) {
        String name = (request.getFutureContext() != null && request.getFutureContext().getName() != null)
                ? request.getFutureContext().getName().toLowerCase() : "";
        if (name.contains("space") || name.contains("우주") || name.contains("무중력")) {
            return "Space_Travel";
        }
        if (name.contains("city") || name.contains("도시") || name.contains("hyper") || name.contains("하이퍼")) {
            return "Hyper_City";
        }
        if (name.contains("virtual") || name.contains("가상") || name.contains("dimension") || name.contains("디지털")) {
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