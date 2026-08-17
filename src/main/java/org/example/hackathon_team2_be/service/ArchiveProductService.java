package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.domain.ArchiveProduct;
import org.example.hackathon_team2_be.dto.*;
import org.example.hackathon_team2_be.repository.ArchiveProductRepository;
import org.example.hackathon_team2_be.repository.HeritageDnaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArchiveProductService {

    private final ArchiveProductRepository archiveProductRepository;
    private final HeritageDnaRepository heritageDnaRepository;

    public ArchiveProductListResponse getProducts() {
        List<ArchiveProductResponse> products = archiveProductRepository.findAll()
                .stream()
                .map(ArchiveProductResponse::from)
                .toList();

        return new ArchiveProductListResponse(products);
    }

    public DnaAnalysisResponse getDnaAnalysis(Long archiveProductId) {
        findProduct(archiveProductId);

        List<DnaAnalysisResponse.DnaItem> dnaAnalysis =
                heritageDnaRepository.findByArchiveProductId(archiveProductId)
                        .stream()
                        .map(DnaAnalysisResponse.DnaItem::from)
                        .toList();

        return new DnaAnalysisResponse(archiveProductId, dnaAnalysis);
    }

    public HeritageLockOptionResponse getHeritageLockOptions(Long archiveProductId) {
        findProduct(archiveProductId);

        List<HeritageLockOptionResponse.HeritageLockOption> options =
                heritageDnaRepository.findByArchiveProductId(archiveProductId)
                        .stream()
                        .map(HeritageLockOptionResponse.HeritageLockOption::from)
                        .toList();

        return new HeritageLockOptionResponse(archiveProductId, options);
    }

    private ArchiveProduct findProduct(Long archiveProductId) {
        return archiveProductRepository.findById(archiveProductId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 아카이브 제품입니다.")
                );
    }
}