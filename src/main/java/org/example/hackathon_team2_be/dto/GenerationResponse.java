package org.example.hackathon_team2_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.hackathon_team2_be.domain.Generation;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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


    // 생성 중
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

    // 생성 완료
    public static GenerationResponse completed(
            Generation generation,
            List<String> lockedDna,
            String futureContext
    ) {
        return new GenerationResponse(
                generation.getId(),
                generation.getStatus().name(),
                generation.getProductName(),
                generation.getCategory(),
                generation.getImageUrl(),
                generation.getDescription(),
                lockedDna,
                futureContext,
                null
        );
    }

    // 생성 실패
    // 생성 실패
    public static GenerationResponse failed(
            Generation generation,
            String message
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
                message
        );
    }
}
