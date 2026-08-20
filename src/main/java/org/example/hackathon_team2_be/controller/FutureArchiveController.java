package org.example.hackathon_team2_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.ApiResponse;
import org.example.hackathon_team2_be.dto.FutureArchiveListResponse;
import org.example.hackathon_team2_be.dto.FutureArchiveResponse;
import org.example.hackathon_team2_be.dto.GenerationResponse;
import org.example.hackathon_team2_be.service.FutureArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FutureArchiveController {

    private final FutureArchiveService futureArchiveService;

    // Future Archive 저장
    @PostMapping("/generations/{generationId}/save")
    public ResponseEntity<ApiResponse<Map<String, Long>>> saveFutureArchive(
            @PathVariable Long generationId
    ) {

        Long futureArchiveId =
                futureArchiveService.saveFutureArchive(generationId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        Map.of("futureArchiveId", futureArchiveId)
                )
        );
    }

    // Future Archive 목록 조회
    @GetMapping("/future-archives")
    public ResponseEntity<FutureArchiveListResponse> getFutureArchives() {

        return ResponseEntity.ok(
                futureArchiveService.getFutureArchives()
        );
    }

    // Future Archive 상세 조회
    @GetMapping("/future-archives/{id}")
    public ResponseEntity<GenerationResponse> getFutureArchive(
            @PathVariable("id") Long generationId
    ) {

        return ResponseEntity.ok(
                futureArchiveService.getFutureArchive(generationId)
        );
    }

}
