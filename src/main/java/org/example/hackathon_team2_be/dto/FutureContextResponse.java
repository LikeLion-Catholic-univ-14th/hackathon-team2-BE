package org.example.hackathon_team2_be.dto;

import org.example.hackathon_team2_be.domain.FutureContext;

public record FutureContextResponse(
        Long id,
        String name,
        String description
) {
    public static FutureContextResponse from(FutureContext futureContext) {
        return new FutureContextResponse(
                futureContext.getId(),
                futureContext.getName(),
                futureContext.getDescription()
        );
    }
}