package org.example.hackathon_team2_be.generation.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_team2_be.generation.dto.DnaDto;
import org.example.hackathon_team2_be.generation.dto.GenerationRequestDto;
import org.example.hackathon_team2_be.generation.dto.OpenAiResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenAiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public OpenAiClient(
            @Value("${ai.openai.api-key:}") String apiKey,
            @Value("${ai.openai.base-url:https://api.openai.com/v1}") String baseUrl,
            @Value("${ai.openai.model:gpt-4o-mini}") String model,
            @Autowired(required = false) ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey;
        this.model = model;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public OpenAiResultDto generateProductDesign(GenerationRequestDto request) {
        if (apiKey == null || apiKey.isBlank() || apiKey.contains("YOUR_ACTUAL")) {
            log.warn("AI API Key is missing or invalid. Skipping AI API call.");
            throw new IllegalStateException("AI API Key is not configured.");
        }

        String systemPrompt = """
                You are an elite futuristic Chief Product Designer for the luxury fashion brand MCM in the year 2076.
                Your role is to blend MCM's rich heritage DNA with future mobility environments to invent high-fashion products.

                [RULES]
                1. Output MUST strictly be a single valid JSON object. No conversational text or markdown code blocks before or after.
                2. The response must contain the following keys:
                   - "productName": A stylish, high-fashion product name combining the base product name, futuristic English keywords, and '2076' (e.g., "MCM AERO STARK 2076").
                   - "category": An elegant, futuristic product category name in English (e.g., "Adaptive Space Mobility Bag").
                   - "imagePrompt": A single flowing natural English description. MUST strictly start with: "Commercial studio product packshot of an empty standalone product resting on a minimalist white pedestal, strictly zero humans, no people, no models, no mannequin, no hands." Then describe the product details: blend classic cognac MCM Visetos monogram leather with futuristic translucent polymer, neon piping, matte carbon fiber, and titanium hardware based on user choices, in a clean white studio with soft diffused lighting and subtle cyan glow. High-end 8k commercial product photography.
                   - "description": A 2 to 3 sentence luxurious storytelling description in Korean. Explain seamlessly how the chosen MCM heritage DNA and the 2076 environment interact. Use a refined, premium fashion brand tone.

                [JSON Output Format]
                {
                  "productName": "...",
                  "category": "...",
                  "imagePrompt": "...",
                  "description": "..."
                }
                """;

        String userPrompt = buildUserPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "response_format", Map.of("type", "json_object")
        );

        log.info("Sending request to AI API (model: {})...", model);

        String responseString = restClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(responseString);
            String jsonContent = root.path("choices").get(0).path("message").path("content").asText();
            log.info("Received AI JSON Response: {}", jsonContent);
            return objectMapper.readValue(jsonContent, OpenAiResultDto.class);
        } catch (Exception e) {
            log.error("Failed to parse AI API response", e);
            throw new RuntimeException("Parsing AI response failed", e);
        }
    }

    private String buildUserPrompt(GenerationRequestDto request) {
        String productName = (request != null && request.getProduct() != null && request.getProduct().getName() != null)
                ? request.getProduct().getName() : "Unknown Product";
        String productDesc = (request != null && request.getProduct() != null && request.getProduct().getDescription() != null)
                ? request.getProduct().getDescription() : "";

        String lockedDnaStr = "None";
        if (request != null && request.getLockedDna() != null && !request.getLockedDna().isEmpty()) {
            lockedDnaStr = request.getLockedDna().stream()
                    .map(dna -> dna.getName() + (dna.getDescription() != null ? ": " + dna.getDescription() : ""))
                    .collect(Collectors.joining(", "));
        }

        String contextName = (request != null && request.getFutureContext() != null && request.getFutureContext().getName() != null)
                ? request.getFutureContext().getName() : "Future World";
        String contextDesc = (request != null && request.getFutureContext() != null && request.getFutureContext().getDescription() != null)
                ? request.getFutureContext().getDescription() : "";

        return String.format("""
                Please design a 2076 MCM Future Product based on these selected options:
                - Base Archive Product: %s (%s)
                - Locked Heritage DNA: %s
                - 2076 Future Environment: %s (%s)
                """, productName, productDesc, lockedDnaStr, contextName, contextDesc);
    }
}
