package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.domain.Generation;
import org.example.hackathon_team2_be.domain.GenerationLockedDna;
import org.example.hackathon_team2_be.domain.GenerationStatus;
import org.example.hackathon_team2_be.dto.FutureArchiveListResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.repository.GenerationLockedDnaRepository;
import org.example.hackathon_team2_be.repository.GenerationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureArchiveService {

    private final GenerationRepository generationRepository;
    private final GenerationLockedDnaRepository generationLockedDnaRepository;

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
    public List<FutureArchiveListResponse> getFutureArchives() {

        List<Generation> generations =
                generationRepository.findAllBySavedAtIsNotNullOrderBySavedAtDesc();

        return generations.stream()
                .map(generation -> {

                    List<GenerationLockedDna> lockedDnas =
                            generationLockedDnaRepository
                                    .findAllByIdGenerationId(generation.getId());

                    List<String> lockedDnaNames = lockedDnas.stream()
                            // TODO:
                            // HeritageDnaRepository를 연결한 뒤
                            // DNA 이름으로 변환
                            .map(dna -> "TODO")
                            .toList();

                    // TODO:
                    // FutureContextRepository를 연결한 뒤
                    // FutureContext 이름 조회

                    return new FutureArchiveListResponse(
                            generation.getId(),
                            generation.getProductName(),
                            generation.getImageUrl(),
                            "TODO",
                            lockedDnaNames,
                            generation.getSavedAt()
                    );
                })
                .toList();
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

        // TODO:
        // HeritageDnaRepository + FutureContextRepository 연결 후
        // lockedDna / futureContext 조회

        return GenerationResponse.completed(
                generation,
                null,
                null
        );
    }
}
