package org.example.hackathon_team2_be.generation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiResultDto {
    private String productName;
    private String category;
    private String imagePrompt;
    private String description;
}
