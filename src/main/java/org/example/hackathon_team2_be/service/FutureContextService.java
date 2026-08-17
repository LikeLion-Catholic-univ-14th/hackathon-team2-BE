package org.example.hackathon_team2_be.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_team2_be.dto.FutureContextListResponse;
import org.example.hackathon_team2_be.dto.FutureContextResponse;
import org.example.hackathon_team2_be.repository.FutureContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FutureContextService {

    private final FutureContextRepository futureContextRepository;

    public FutureContextListResponse getFutureContexts() {
        List<FutureContextResponse> futureContexts = futureContextRepository.findAll()
                .stream()
                .map(FutureContextResponse::from)
                .toList();

        return new FutureContextListResponse(futureContexts);
    }
}
