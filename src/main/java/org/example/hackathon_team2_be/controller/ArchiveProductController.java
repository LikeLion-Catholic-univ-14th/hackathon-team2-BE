package org.example.hackathon_team2_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.ApiResponse;
import org.example.hackathon_team2_be.dto.ArchiveProductListResponse;
import org.example.hackathon_team2_be.dto.DnaAnalysisResponse;
import org.example.hackathon_team2_be.dto.HeritageLockOptionResponse;
import org.example.hackathon_team2_be.service.ArchiveProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archive-products")
public class ArchiveProductController {

    private final ArchiveProductService archiveProductService;

    @GetMapping
    public ResponseEntity<ApiResponse<ArchiveProductListResponse>> getProducts() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        archiveProductService.getProducts()
                )
        );
    }

    @GetMapping("/{archiveProductId}/dna-analysis")
    public DnaAnalysisResponse getDnaAnalysis(
            @PathVariable Long archiveProductId
    ) {
        return archiveProductService.getDnaAnalysis(archiveProductId);
    }

    @GetMapping("/{archiveProductId}/heritage-lock-options")
    public HeritageLockOptionResponse getHeritageLockOptions(
            @PathVariable Long archiveProductId
    ) {
        return archiveProductService.getHeritageLockOptions(archiveProductId);
    }
}