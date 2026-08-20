package org.example.hackathon_team2_be.generation.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GeminiImageClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public GeminiImageClient(
            @Value("${ai.gemini.api-key:}") String apiKey,
            @Value("${ai.gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
            @Value("${ai.gemini.model:gemini-2.5-flash-image}") String model,
            @Autowired(required = false) ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.model = model;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Gemini 멀티모달 이미지 생성 모델 (gemini-2.5-flash-image / gemini-3.1-flash-image 등) API 호출
     */
    public String generateImage(String prompt) {
        if (apiKey == null || apiKey.isBlank() || apiKey.contains("YOUR_ACTUAL")) {
            log.warn("Gemini API Key is missing or invalid.");
            throw new IllegalStateException("Gemini API Key is not configured.");
        }

        log.info("Sending request to Google Gemini Image API (model: {})...", model);

        try {
            // Imagen 모델(:predict) vs Gemini 멀티모달 모델(:generateContent) 분기 지원
            boolean isImagenPredict = model.startsWith("imagen-");

            String endpoint;
            Object requestBody;

            if (isImagenPredict) {
                endpoint = "/models/" + model + ":predict?key=" + apiKey;
                requestBody = Map.of(
                        "instances", List.of(
                                Map.of("prompt", prompt)
                        ),
                        "parameters", Map.of(
                                "sampleCount", 1,
                                "aspectRatio", "1:1",
                                "personGeneration", "DONT_ALLOW",
                                "outputOptions", Map.of("mimeType", "image/jpeg")
                        )
                );
            } else {
                endpoint = "/models/" + model + ":generateContent?key=" + apiKey;
                requestBody = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(
                                        Map.of("text", prompt)
                                ))
                        )
                );
            }

            String responseString = restClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(responseString);

            // 1. Gemini generateContent 응답 (candidates[0].content.parts[].inlineData)
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray()) {
                    for (JsonNode part : parts) {
                        JsonNode inlineData = part.has("inlineData") ? part.get("inlineData") : part.get("inline_data");
                        if (inlineData != null && !inlineData.isMissingNode()) {
                            String base64Bytes = inlineData.path("data").asText();
                            String mimeType = inlineData.path("mimeType").asText("image/jpeg");
                            if (base64Bytes != null && !base64Bytes.isBlank()) {
                                String dataUrl = "data:" + mimeType + ";base64," + base64Bytes;
                                log.info("Successfully generated image with Gemini Image model (length: {} chars)", dataUrl.length());
                                return dataUrl;
                            }
                        }
                    }
                }
            }

            // 2. Imagen predict 응답 (predictions[0].bytesBase64Encoded)
            JsonNode predictions = root.path("predictions");
            if (predictions.isArray() && !predictions.isEmpty()) {
                String base64Bytes = predictions.get(0).path("bytesBase64Encoded").asText();
                String mimeType = predictions.get(0).path("mimeType").asText("image/jpeg");
                String dataUrl = "data:" + mimeType + ";base64," + base64Bytes;
                log.info("Successfully generated image with Google Imagen 3 (length: {} chars)", dataUrl.length());
                return dataUrl;
            }

            throw new RuntimeException("No valid image data found in Gemini response: " + responseString);

        } catch (Exception e) {
            log.warn("Failed to generate image with Google Gemini Image API: {}", e.getMessage());
            throw new RuntimeException("Google Gemini Image generation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 하위 호환성을 위한 메서드
     */
    public String generateImagen3Image(String prompt) {
        return generateImage(prompt);
    }
}