package org.example.hackathon_team2_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GenerationCreateRequest {

    private Long archiveProductId;

    private List<Long> lockedDnaIds;

    private Long futureContextId;

}
