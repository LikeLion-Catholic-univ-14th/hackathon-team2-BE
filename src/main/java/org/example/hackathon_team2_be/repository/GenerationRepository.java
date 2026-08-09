package org.example.hackathon_team2_be.repository;

import org.example.hackathon_team2_be.domain.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerationRepository extends JpaRepository<Generation, Long> {
}
