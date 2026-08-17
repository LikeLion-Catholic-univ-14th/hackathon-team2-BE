package org.example.hackathon_team2_be.repository;

import org.example.hackathon_team2_be.domain.GenerationLockedDna;
import org.example.hackathon_team2_be.domain.GenerationLockedDnaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenerationLockedDnaRepository extends JpaRepository<GenerationLockedDna, GenerationLockedDnaId> {
    List<GenerationLockedDna> findAllByIdGenerationId(Long generationId);
}
