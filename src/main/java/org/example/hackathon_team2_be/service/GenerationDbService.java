package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.domain.*;
import org.example.hackathon_team2_be.dto.AiGenerationResponse;
import org.example.hackathon_team2_be.dto.GenerationCreateRequest;
import org.example.hackathon_team2_be.dto.GenerationCreateResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.generation.dto.*;
import org.example.hackathon_team2_be.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GenerationDbService {

    private final GenerationRepository generationRepository;
    private final GenerationLockedDnaRepository generationLockedDnaRepository;
    private final ArchiveProductRepository archiveProductRepository;
    private final HeritageDnaRepository heritageDnaRepository;
    private final FutureContextRepository futureContextRepository;


    //생성 요청
    @Transactional
    public GenerationCreateResponse createGeneration(GenerationCreateRequest request){

        //상품 조회
        ArchiveProduct product = archiveProductRepository.findById(request.getArchiveProductId()).
                orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        //futureContext 조회
        FutureContext futureContext = futureContextRepository
                .findById(request.getFutureContextId()).orElseThrow(() -> new IllegalArgumentException("Future Context를 찾을 수 없습니다."));


        //generation 생성
        Generation generation = new Generation(
                product.getId(),
                futureContext.getId()
        );

        generationRepository.save(generation);

        //선택된 dna 저장
        for (Long dnaId : request.getLockedDnaIds()) {

            HeritageDna dna = heritageDnaRepository
                    .findById(dnaId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException("heritage dna를 찾을 수 없습니다."));


            generationLockedDnaRepository.save(
                    new GenerationLockedDna(
                            generation.getId(),
                            dna.getId()
                    )
            );
        }


        return GenerationCreateResponse.from(generation);

    }

    // AI 생성 결과 조회
    @Transactional(readOnly = true)
    public GenerationResponse getGeneration(Long generationId){

        Generation generation = generationRepository
                .findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "생성 결과를 찾을 수 없습니다."
                        )
                );
        // 아직 생성 중
        if (generation.getStatus() == GenerationStatus.GENERATING) {
            return GenerationResponse.generating(generation);
        }

        // 생성 실패
        if (generation.getStatus() == GenerationStatus.FAILED) {
            return GenerationResponse.failed(generation, "생성에 실패했습니다");
        }

        //생성 완료
        List<GenerationLockedDna> lockedDnas =
                generationLockedDnaRepository.findAllByIdGenerationId(generation.getId());


        List<String> lockedDnaNames = lockedDnas.stream()
                .map(dna -> heritageDnaRepository.findById(
                        dna.getId().getHeritageDnaId()
                ))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(HeritageDna::getName)
                .toList();

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



    @Transactional
    public void completeGeneration(
            Long generationId,
            GenerationResponseDto response
    ) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("생성 결과를 찾을 수 없습니다.")
                );

        generation.complete(
                response.getProductName(),
                response.getCategory(),
                response.getImageUrl(),
                response.getDescription()
        );
    }

    @Transactional(readOnly = true)
    public GenerationRequestDto createAiRequest(
            GenerationCreateRequest request
    ) {
        // 상품 조회
        ArchiveProduct product = archiveProductRepository
                .findById(request.getArchiveProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException("상품을 찾을 수 없습니다.")
                );

        // Future Context 조회
        FutureContext futureContext = futureContextRepository
                .findById(request.getFutureContextId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Future Context를 찾을 수 없습니다.")
                );

        // ProductDto 변환
        ProductDto productDto = ProductDto.builder()
                .name(product.getName())
                .description(product.getShortDescription())
                .build();

        // ContextDto 변환
        ContextDto contextDto = ContextDto.builder()
                .name(futureContext.getName())
                .description(futureContext.getDescription())
                .build();

        // DnaDto 변환
        List<DnaDto> dnaDtos = request.getLockedDnaIds()
                .stream()
                .map(id -> heritageDnaRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "heritage dna를 찾을 수 없습니다. id=" + id
                                )
                        ))
                .map(dna -> DnaDto.builder()
                        .name(dna.getName())
                        .description(dna.getDescription())
                        .build())
                .toList();

        // GenerationRequestDto 생성
        return GenerationRequestDto.builder()
                .product(productDto)
                .lockedDna(dnaDtos)
                .futureContext(contextDto)
                .build();
    }

    @Transactional
    public void failGeneration(Long generationId) {
        Generation generation = generationRepository.findById(generationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("생성 결과를 찾을 수 없습니다.")
                );

        generation.fail();
    }

}
