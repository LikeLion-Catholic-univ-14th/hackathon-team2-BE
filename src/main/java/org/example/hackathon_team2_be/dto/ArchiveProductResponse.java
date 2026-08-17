package org.example.hackathon_team2_be.dto;

import org.example.hackathon_team2_be.domain.ArchiveProduct;
import org.example.hackathon_team2_be.domain.ArchiveProductTag;

import java.util.List;

public record ArchiveProductResponse(
        Long id,
        String name,
        String shortDescription,
        List<String> tags
) {
    public static ArchiveProductResponse from(ArchiveProduct product) {
        return new ArchiveProductResponse(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getTags().stream()
                        .map(ArchiveProductTag::getTagName)
                        .toList()
        );
    }
}