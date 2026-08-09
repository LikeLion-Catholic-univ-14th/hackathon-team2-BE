package org.example.hackathon_team2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.hackathon_team2_be.domain.Generation;


@Getter
@AllArgsConstructor
public class GenerationCreateResponse {

    private Long generationId;

    private String status;

    public static GenerationCreateResponse from(Generation generation) {
        return new GenerationCreateResponse(
                generation.getId(),
                generation.getStatus().name()
        );
    }
}
