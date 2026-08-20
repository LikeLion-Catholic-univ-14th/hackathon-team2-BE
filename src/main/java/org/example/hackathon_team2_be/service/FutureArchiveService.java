package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.domain.*;
import org.example.hackathon_team2_be.dto.ArchiveInsight;
import org.example.hackathon_team2_be.dto.FutureArchiveListResponse;
import org.example.hackathon_team2_be.dto.FutureArchiveResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.repository.FutureContextRepository;
import org.example.hackathon_team2_be.repository.GenerationLockedDnaRepository;
import org.example.hackathon_team2_be.repository.GenerationRepository;
import org.example.hackathon_team2_be.repository.HeritageDnaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FutureArchiveService {

    private final GenerationRepository generationRepository;
    private final GenerationLockedDnaRepository generationLockedDnaRepository;
    private final HeritageDnaRepository heritageDnaRepository;
    private final FutureContextRepository  futureContextRepository;

    // Future Archive 저장
    @Transactional
    public Long saveFutureArchive(Long generationId) {

        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("생성 결과를 찾을 수 없습니다."));
        if (generation.getStatus() != GenerationStatus.COMPLETED) {
            throw new IllegalStateException("생성이 완료된 결과만 저장할 수 있습니다.");
        }
        generation.save();
        return generation.getId();
    }

    //Future Archive 목록 조회
    @Transactional(readOnly = true)
    public FutureArchiveListResponse getFutureArchives() {

        List<Generation> generations =
                generationRepository.findAllBySavedAtIsNotNullOrderBySavedAtDesc();

        List<FutureArchiveResponse> archives = generations.stream()
                .map(generation -> {

                    // 해당 generation의 locked DNA 조회
                    List<GenerationLockedDna> lockedDnas =
                            generationLockedDnaRepository
                                    .findAllByIdGenerationId(generation.getId());

                    List<String> lockedDnaNames = lockedDnas.stream()
                            .map(dna -> heritageDnaRepository.findById(
                                    dna.getId().getHeritageDnaId()
                            ))
                            .filter(java.util.Optional::isPresent)
                            .map(java.util.Optional::get)
                            .map(HeritageDna::getName)
                            .toList();

                    // Future Context 조회
                    FutureContext futureContext =
                            futureContextRepository.findById(
                                    generation.getFutureContextId()
                            ).orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Future Context를 찾을 수 없습니다."
                                    )
                            );

                    return new FutureArchiveResponse(
                            generation.getId(),
                            generation.getProductName(),
                            generation.getImageUrl(),
                            futureContext.getName(),
                            lockedDnaNames,
                            generation.getSavedAt()
                    );
                })
                .toList();

        // 가장 많이 선택된 DNA
        String mostSelectedDna = archives.stream()
                .flatMap(archive -> archive.getLockedDna().stream())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // 가장 많이 선택된 Future Context
        String mostPopularFutureContext = archives.stream()
                .map(FutureArchiveResponse::getFutureContext)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        ArchiveInsight archiveInsight = new ArchiveInsight(
                mostSelectedDna,
                mostPopularFutureContext
        );

        return new FutureArchiveListResponse(
                archives,
                archiveInsight
        );
    }

    // Future Archive 상세 조회
    @Transactional(readOnly = true)
    public GenerationResponse getFutureArchive(Long generationId) {

        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Future Archive를 찾을 수 없습니다."
                        )
                );

        if (generation.getSavedAt() == null) {
            throw new IllegalArgumentException(
                    "저장된 Future Archive가 아닙니다."
            );
        }

        // 해당 generation의 locked DNA 조회
        List<GenerationLockedDna> lockedDnas =
                generationLockedDnaRepository
                        .findAllByIdGenerationId(generation.getId());

        List<String> lockedDnaNames = lockedDnas.stream()
                .map(dna -> heritageDnaRepository.findById(
                        dna.getId().getHeritageDnaId()
                ))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(HeritageDna::getName)
                .toList();

        // Future Context 조회
        FutureContext futureContext =
                futureContextRepository.findById(
                        generation.getFutureContextId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Future Context를 찾을 수 없습니다."
                        )
                );

        return GenerationResponse.completed(
                generation,
                lockedDnaNames,
                futureContext.getName()
        );
    }
}
