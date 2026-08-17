package org.example.hackathon_team2_be.dto;

import java.util.List;

public record FutureContextListResponse(
        List<FutureContextResponse> futureContexts
) {
}