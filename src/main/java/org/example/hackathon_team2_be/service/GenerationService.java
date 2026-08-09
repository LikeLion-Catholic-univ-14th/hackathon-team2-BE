package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.domain.Generation;
import org.example.hackathon_team2_be.domain.GenerationLockedDna;
import org.example.hackathon_team2_be.domain.GenerationStatus;
import org.example.hackathon_team2_be.dto.AiGenerationResponse;
import org.example.hackathon_team2_be.dto.GenerationCreateRequest;
import org.example.hackathon_team2_be.dto.GenerationCreateResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.repository.GenerationLockedDnaRepository;
import org.example.hackathon_team2_be.repository.GenerationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerationService {

    private final GenerationRepository generationRepository;
    private final GenerationLockedDnaRepository generationLockedDnaRepository;

    @Transactional
    public GenerationCreateResponse createGeneration(GenerationCreateRequest request){
        // TODO
        // ArchiveProduct 조회
        // FutureContext 조회
        // HeritageDna 조회
        Generation generation = new Generation(
                request.getArchiveProductId(),
                request.getFutureContextId()
        );

        generationRepository.save(generation);

        for (Long dnaId : request.getLockedDnaIds()) {

            generationLockedDnaRepository.save(
                    new GenerationLockedDna(
                            generation.getId(),
                            dnaId
                    )
            );
        }

        /*
         * TODO
         *
         * DB에서 조회한 실제 데이터를
         * AiGenerationRequest로 만들어서
         * 백엔드 2에 전달.
         *
         * 백엔드 2의 AI 생성은 여기서 하지 않음.
         *
         * 백엔드 2가 생성 결과를 다시 보내오면
         * Generation.complete()을 호출해서
         * DB를 COMPLETED로 변경.
         */

        return GenerationCreateResponse.from(generation);

    }

    @Transactional(readOnly = true)
    public GenerationResponse getGeneration(Long generationId){

        Generation generation = generationRepository
                .findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "생성 결과를 찾을 수 없습니다."
                        )
                );

        if (generation.getStatus() == GenerationStatus.GENERATING) {
            return GenerationResponse.generating(generation);
        }

        // TODO
        // COMPLETED / FAILED 결과 응답 구현

        return null;
    }

    @Transactional
    public void completeGeneration(
            Long generationId,
            AiGenerationResponse response
    ) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "생성 결과를 찾을 수 없습니다."
                        )
                );

        generation.complete(
                response.getProductName(),
                response.getCategory(),
                response.getImageUrl(),
                response.getDescription()
        );
    }

}
