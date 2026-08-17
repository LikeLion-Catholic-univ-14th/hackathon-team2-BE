package org.example.hackathon_team2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class FutureArchiveResponse {

    private Long id; //generationId
    private String productName;
    private String imageUrl;
    private String futureContext;
    private List<String> lockedDna;
    private LocalDateTime createdAt;


}
