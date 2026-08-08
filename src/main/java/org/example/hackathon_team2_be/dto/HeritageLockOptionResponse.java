package org.example.hackathon_team2_be.dto;

import org.example.hackathon_team2_be.domain.HeritageDna;

import java.util.List;

public record HeritageLockOptionResponse(
        Long archiveProductId,
        List<HeritageLockOption> heritageLockOptions
) {
    public record HeritageLockOption(
            Long id,
            String name,
            String description
    ) {
        public static HeritageLockOption from(HeritageDna dna) {
            return new HeritageLockOption(
                    dna.getId(),
                    dna.getName(),
                    dna.getDescription()
            );
        }
    }
}