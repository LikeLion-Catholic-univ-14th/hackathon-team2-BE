package org.example.hackathon_team2_be.dto;

import org.example.hackathon_team2_be.domain.HeritageDna;

import java.util.List;

public record DnaAnalysisResponse(
        Long archiveProductId,
        List<DnaItem> dnaAnalysis
) {
    public record DnaItem(
            String name,
            int ratio
    ) {
        public static DnaItem from(HeritageDna dna) {
            return new DnaItem(dna.getName(), dna.getRatio());
        }
    }
}