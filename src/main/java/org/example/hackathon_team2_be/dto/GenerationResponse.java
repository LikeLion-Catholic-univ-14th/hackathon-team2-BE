package org.example.hackathon_team2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.hackathon_team2_be.domain.Generation;

import java.util.List;

@Getter
@AllArgsConstructor
public class GenerationResponse {

    private Long generationId;

    private String status;

    private String productName;

    private String category;

    private String imageUrl;

    private String description;

    private List<String> lockedDna;

    private String futureContext;

    private String message;

    public static GenerationResponse generating(
            Generation generation
    ) {
        return new GenerationResponse(
                generation.getId(),
                generation.getStatus().name(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
