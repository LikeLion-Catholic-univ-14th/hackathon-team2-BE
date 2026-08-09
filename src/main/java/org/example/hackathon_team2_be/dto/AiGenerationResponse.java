package org.example.hackathon_team2_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiGenerationResponse {
    private String productName;
    private String category;
    private String imageUrl;
    private String description;

}
