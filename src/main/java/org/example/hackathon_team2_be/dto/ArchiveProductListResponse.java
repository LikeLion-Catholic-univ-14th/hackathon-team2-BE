package org.example.hackathon_team2_be.dto;

import java.util.List;

public record ArchiveProductListResponse(
        List<ArchiveProductResponse> products
) {
}