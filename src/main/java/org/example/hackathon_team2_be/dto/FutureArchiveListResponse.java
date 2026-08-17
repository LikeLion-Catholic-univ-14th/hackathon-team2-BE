package org.example.hackathon_team2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FutureArchiveListResponse {

    private List<FutureArchiveResponse> archives;
    private ArchiveInsight archiveInsight;
}
