package org.example.hackathon_team2_be.generation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerationRequestDto {
    private ProductDto product;
    private List<DnaDto> lockedDna;
    private ContextDto futureContext;
}
