package org.example.hackathon_team2_be.generation;

import org.example.hackathon_team2_be.generation.dto.ContextDto;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.ProductDto;
import org.example.hackathon_team2_be.generation.service.ScenarioImageResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioImageResolverTest {

    private ScenarioImageResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ScenarioImageResolver();
        resolver.init();
    }

    @Test
    @DisplayName("Ottomar + Space + [VISETOS, MOBILITY] -> 05번 이미지 매칭 검증")
    void testOttomarSpaceVisetosMobility() {
        GenerationRequestDto request = GenerationRequestDto.builder()
                .product(ProductDto.builder().name("Ottomar 비세토스 위켄더").build())
                .futureContext(ContextDto.builder().name("Space Travel").build())
                .lockedDna(List.of(
                        DnaDto.builder().name("VISETOS").build(),
                        DnaDto.builder().name("MOBILITY").build()
                ))
                .build();

        Optional<String> url = resolver.resolveScenarioImageUrl(request);
        assertThat(url).isPresent();
        assertThat(url.get()).contains("05_Ottomar_Boston_weekender_travel_bag_VISETOS_MOBILITY_Space_Travel.png");
    }

    @Test
    @DisplayName("Stark + Hyper City + [VISETOS, METAL STUDS] -> 46번 이미지 매칭 검증")
    void testStarkHyperCityVisetosMetalStuds() {
        GenerationRequestDto request = GenerationRequestDto.builder()
                .product(ProductDto.builder().name("Stark 사이드 비세토스 백팩").build())
                .futureContext(ContextDto.builder().name("Hyper City").build())
                .lockedDna(List.of(
                        DnaDto.builder().name("VISETOS").build(),
                        DnaDto.builder().name("METAL STUDS").build()
                ))
                .build();

        Optional<String> url = resolver.resolveScenarioImageUrl(request);
        assertThat(url).isPresent();
        assertThat(url.get()).contains("46_Stark_backpack_VISETOS_METAL STUDS_Hyper_City.png");
    }

    @Test
    @DisplayName("We The Best 파우치 + Virtual Dimension + [VISETOS, MIAMI BLUE] -> 86번 이미지 매칭 검증")
    void testPouchVirtualVisetosMiamiBlue() {
        GenerationRequestDto request = GenerationRequestDto.builder()
                .product(ProductDto.builder().name("SMCM X We The Best 비세토스 크로스바디 파우치").build())
                .futureContext(ContextDto.builder().name("Virtual Dimension").build())
                .lockedDna(List.of(
                        DnaDto.builder().name("VISETOS").build(),
                        DnaDto.builder().name("MIAMI BLUE").build()
                ))
                .build();

        Optional<String> url = resolver.resolveScenarioImageUrl(request);
        assertThat(url).isPresent();
        assertThat(url.get()).contains("86_crossbody_pouch_bag_VISETOS_MIAMI BLUE_Virtual_Dimension.png");
    }
}