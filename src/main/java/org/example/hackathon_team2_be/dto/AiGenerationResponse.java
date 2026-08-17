package org.example.hackathon_team2_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiGenerationResponse {
    // 백엔드 2 -> 백엔드 1 응답

    private String productName;
    private String category;
    private String imageUrl;
    private String description;

}
